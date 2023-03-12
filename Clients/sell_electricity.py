# importing the requests library
import requests
import random
import time
import sys
import argparse
import names
import datetime
import json
import threading
import os

API_CUSTOMERSERVICE_SELL_ELECTRICITY = f'http://{os.environ.get("API_ORCHESTRATOR_HOST", "localhost")}:{os.environ.get("API_ORCHESTRATOR_PORT", "8080")}'

def sell_electricity():
    return None

def convert_selection(selection):
    if selection == 1:
        return 'COMPANY'
    else:
        return 'PRIVATE_PERSON'



if __name__ == '__main__' :
    parser = argparse.ArgumentParser()
    parser.add_argument("--id", help="id du client :", type=str)

    parser.add_argument("--quantity", help="quantité de l'écergie :", type=str)
    parser.add_argument("--price", help="prix de l'écergie :", type=str)

    args=parser.parse_args()

    


    
    print("Enter energy's quantity:")
    quantity = int(input())

    print("Enter energy's price:")
    price = int(input())

    print("Enter customer's name:")
    customer_name = str(input())

    res=requests.post(f"{API_CUSTOMERSERVICE_SELL_ELECTRICITY}/sell-electrity/" + customer_name, json={
            'quantity': quantity,
            'price': price
        }
    )

    if 200 <= res.status_code <= 299 :
        print("energy has been sold ...")
        print("the price will be deduced from ypur bill next month ;-)")

        print("\nBest regards. \nSmartrixgrid team")



    