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




API_ORCHESTRATOR = f'http://{os.environ.get("API_ORCHESTRATOR_HOST", "localhost")}:{os.environ.get("API_ORCHESTRATOR_PORT", "8080")}'
API_CUSTOMERS = f'http://{os.environ.get("API_CUSTOMERS_HOST", "localhost")}:{os.environ.get("API_CUSTOMERS_PORT", "8080")}'

API_MEASURES_ENDPOINT =f"http://{os.environ.get('API_MEASURES_HOST', 'localhost')}:{os.environ.get('API_MEASURES_PORT', '5004')}+/measures"






def create_random_company_customer():

    random_name = names.get_last_name()+' Electric'
    res = requests.post(url=API_ORCHESTRATOR, json={
        "name": random_name,
        "type": "COMPANY",
    })
    if res.status_code in range(200, 299):
        tmp = res.json()
        print('Created company customer', random_name, 'with id', tmp["id"])
        return tmp
    else:
        print("Failed to create a random company customer", res.status_code)


def send_charging_station(charging_station):
    res=requests.post(url=f'{API_ORCHESTRATOR}/charging-stations',json=charging_station)
    if res.status_code==202:
        charging_station=res.json()
        print('Created charging station ', charging_station['name'], 'with id', charging_station['id'])
        return charging_station
    else:
        print('Failed to create charging station', charging_station['name'])


        



def send_measure(measure: dict):
    res = requests.post(url=API_MEASURES_ENDPOINT, json=measure)
    if res.status_code in range(200, 299):
        return True
    else:
        print('Failed to send measure to SmartrixGrid')
        return False


def simulate_electric_meter(customer):
    while True:
        time.sleep(1)
        b = send_measure({
            "customerId": customer['id'],
            "timestamp": f'{datetime.datetime.now().replace(microsecond=0).isoformat()}',
            "energyUsed": random.randint(1, 10)
        })
        if not b:
            break


def generate_random_history(customer, past_duration: datetime.timedelta):
    """
    This method will generate random consumption history for the given customer
    """
    upper = datetime.datetime.now()
    lower = upper - past_duration

    current = lower

    while current < upper:
        b = send_measure({
            "customerId": customer['id'],
            "timestamp": f'{current.replace(microsecond=0).isoformat()}',
            "energyUsed": random.randint(1, 10)
        })
        if not b:
            break
        current = current + datetime.timedelta(seconds=5)




if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("nbcustomer", help="Nombre de consommateurs", type=int)
    parser.add_argument('--simulate',action="store_true", help="Continuer à envoyer des measures pour simuler un consommateur")
    parser.add_argument('--history', type=int, help="Sur combien d'heures il faut générer une historique pour les clients")

    args=parser.parse_args()



    for i in range(0,args.nbcustomer):
        customer = create_random_customer()
        if customer != None:
            threading.Thread(target=simulate_electric_meter, args=[customer]).start()
            print('-')
            
            threading.Thread(target=generate_random_history, args=[customer, datetime.timedelta(hours=1)]).start()
            #threading.Thread(target=simulate_electric_meter, args=[customer]).start()



    
    

    


