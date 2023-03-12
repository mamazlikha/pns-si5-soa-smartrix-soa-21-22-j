import requests
import argparse
import sys
import os

API_CONSULT_BILL = "http://" + os.environ.get('API_CUSTOMERS_HOST', 'localhost') + ":" + os.environ.get(
    'API_CUSTOMERS_PORT', '8080') + "/"

def consult_customer_bill(customer_name, graph_type):
    url=f'{API_CONSULT_BILL}/customers/{customer_name}/consumption/graph/{graph_type}'
    print(url)
    res = requests.get(url)

    if res.status_code == 202 :
        result_id= res.json()["resultId"]

        while True: # infinit loop to force programme to wait until the service response
            res=requests.get(url=f'{API_CONSULT_BILL}/graphs/{result_id}')
            if res.status_code==102:
                continue
            elif res.status_code==200:
                return res.json()
            else:
                print(res.status_code)
                

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
    parser.add_argument("--customerName", help="Name of customer", type=str)
    parser.add_argument("--graphType", help="Type of graph", type=int)

    args=parser.parse_args()

    nb_args = len(sys.argv)
    graph_type = None
    customer_name = None

    if nb_args == 1:

        print("Enter customer's name:")
        customer_name = input()

        print("What's graph type, please enter a number :")
        print("1: LAST 30 DAYS")
        print("2: LAST 24 HOUR")
        print("3: LAST 12 MONTH")
        choice = int(input())
        
    elif nb_args == 5 :

        customer_name = args.customerName
        choice = args.graphType
    else: 
        raise Exception('Must enter 2 arguments')

    graph_type = get_graph_type(choice)

    return consult_customer_bill(customer_name, graph_type)


if __name__ == '__main__':
    try:
        bill = main()
        print("bill = ", bill)
    except ValueError:
        print(ValueError)

# Example type in your terminal: python .\consultCustomer.py --customerName "Jennifer Wilson" --graphType 2
# Of course you can change Jennifer Wilson by any other name. Just make sure you surrounded name and surname with ""