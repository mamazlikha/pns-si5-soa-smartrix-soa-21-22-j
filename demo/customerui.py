import requests
import argparse
import sys
import os

API_CUSTOMERS = f'http://{os.environ.get("API_CUSTOMERS_HOST", "localhost")}:{os.environ.get("API_CUSTOMERS_PORT", "8080")}'


def consult_customer_bill(customer_name, graph_type):

    res = requests.get(
        url=f'{API_CUSTOMERS}/customers/{customer_name}/consumption/graph/{graph_type}')


    if res.status_code in range(200, 300):    
        return res.json()        
    else:
        raise Exception('request returned : ', res.status_code)


def get_graph_type(choice):
    if choice == 1:
        return 'LAST_MONTH_BY_DAY'
    elif choice == 2:
        return 'LAST_DAY_BY_HOUR'
    elif choice == 3:
        return 'LAST_YEAR_BY_MONTH'
    else:
        raise Exception(
            "Bad choice! \nYou must select an integer between 1 and 3")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--customerName", help="Name of customer", type=str)
    parser.add_argument("--graphType", help="Type of graph", type=int)
    args = parser.parse_args()

    nb_args = len(sys.argv)
    print(nb_args)
    graph_type = None
    custoemr_name = None

    if nb_args == 0:

        print("Enter customer's name:")
        custoemr_name = input()

        print("What's graph type, please enter a number :")
        print("1: LAST 30 DAYS")
        print("2: LAST 24 HOUR")
        print("3: LAST 12 MONTH")
        choice = int(input())

    elif nb_args == 5:

        custoemr_name = args.customerName
        choice = args.graphType

    else:
        raise Exception('Must enter 2 arguments')

    graph_type = get_graph_type(choice)

    return consult_customer_bill(custoemr_name, graph_type)


if __name__ == '__main__':
    try:
        bill = main()
        print("bill = ", bill)
    except ValueError:
        print(ValueError)
