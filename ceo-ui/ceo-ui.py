import requests
import argparse
import sys,os

API_CONSULT_OVERVIEW = f'http://{os.environ.get("API_ENERGY_MONITOR_HOST", "localhost")}:5004'

def consult_overview(graph_type):

    res = requests.get(url=f'{API_CONSULT_OVERVIEW}/ceo/{graph_type}')

#accepted
    if 200 <= res.status_code and res.status_code <= 299:
        return res.json()
    else :
        raise Exception('request returned : ', res.status_code)


def get_graph_type(choice):
    if choice == 1:
        return 'LAST_MONTH_BY_DAY'
    elif choice == 2:
        return 'LAST_DAY_BY_HOUR'
    elif choice == 3:
        return 'LAST_YEAR_BY_MONTH'
    else:
        raise Exception("Bad choice! \nYou must select an integer between 1 and 3")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--graphType", help="Type of graph", type=int)
    args=parser.parse_args()

    nb_args = len(sys.argv)
    graph_type = None

    if nb_args == 0:
        print("What's graph type, please enter a number :")
        print("1: LAST 30 DAYS")
        print("2: LAST 24 HOURS")
        print("3: LAST 12 MONTHS")
        choice = int(input())
        
    elif nb_args == 3 :

        
        choice = args.graphType
       
    else: 
        raise Exception('Must enter 2 arguments')

    graph_type = get_graph_type(choice)

    return consult_overview(graph_type)


if __name__ == '__main__':
    try:
        bill = main()
        print("bill = ", bill)
    except ValueError:
        print(ValueError)

