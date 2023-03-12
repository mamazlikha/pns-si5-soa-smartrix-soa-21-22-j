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

MAX_GENERATION = 20
# defining the api-endpoint

API_ORCHESTRATOR_URL = os.environ.get(
    "API_ORCHESTRATOR_URL", "http://localhost:8082")
API_CUSTOMERS = f'http://{os.environ.get("API_CUSTOMERS_HOST", "localhost")}:{os.environ.get("API_CUSTOMERS_PORT", "8080")}'
API_MEASURES = f"http://{os.environ.get('API_MEASURES_HOST', 'localhost')}:{os.environ.get('API_MEASURES_PORT', '5004')}"


API_MEASURES_ENDPOINT = API_MEASURES+"/measures"
API_CUSTOMERS_ENDPOINT = API_CUSTOMERS+"/customers"
ACCOUNTING_SERVICE_URL = f"http://{os.environ.get('ACCOUNTING_SERVICE_HOST', 'localhost')}:{os.environ.get('ACCOUNTING_SERVICE_PORT', '5007')}"

ELECTRICITY_METER_REGISTER_SERVICE_URL = f"http://{os.environ.get('LOCAL_PRODUCTION_MONITOR_HOST', 'localhost')}:{os.environ.get('LOCAL_PRODUCTION_MONITOR_PORT', '8083')}"


def str_time_prop(start, end, time_format, prop):
    """Get a time at a proportion of a range of two formatted times.

    start and end should be strings specifying times formatted in the
    given format (strftime-style), giving an interval [start, end].
    prop specifies how a proportion of the interval to be taken after
    start.  The returned time will be in the specified format.
    """

    stime = time.mktime(time.strptime(start, time_format))
    etime = time.mktime(time.strptime(end, time_format))

    ptime = stime + prop * (etime - stime)

    return time.strftime(time_format, time.localtime(ptime))


def random_date(start, end, prop):
    return str_time_prop(start, end, '%m/%d/%Y %I:%M', prop)


def create_customer(name, region='C'):

    res = requests.post(url=API_CUSTOMERS_ENDPOINT, json={
        "name": name,
        "type": "PRIVATE_PERSON",
        "region": region,
        "bankAccount": {
            "iban": "1234.1010.2384.34T3.540",
            "swiftCode": "123456"
        }
    })
    if res.status_code in range(200, 299):
        tmp = res.json()
        print('Created customer', name, 'with id', tmp["id"])
        return tmp
    else:
        print("Failed to create customer ", name, res.status_code)


def create_random_customer():

    random_name = names.get_full_name()
    res = requests.post(url=API_CUSTOMERS_ENDPOINT, json={
        "name": random_name,
        "type": "PRIVATE_PERSON",
        'email': random_name.replace(' ', '.').lower()+'@jmail.com',
        "region": random.choice(['A','B','C']),
        "bankAccount": {
            "iban": f"{random.randint(1000,9999)}.{random.randint(1000,9999)}.{random.randint(1000,9999)}.{random.randint(1000,9999)}.{random.randint(1000,9999)}",
            "swiftCode": f'{random.randint(5000,9999)}'
        }

    })
    if res.status_code in range(200, 299):
        tmp = res.json()
        print('Created customer', random_name, 'with id', tmp["id"])
        return tmp
    else:
        print("Failed to create a random customer", res.status_code)


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





def generate_random_history(customer, lower: datetime.datetime, upper: datetime.datetime, step=datetime.timedelta(seconds=300)):
    """
    This method will generate random consumption history for the given customer
    """

    total = 0
    if not customer:
        return

    current = lower

    while current < upper:

        used = random.randint(0, 30)+1

        total += used
        b = send_measure({
            "customerId": customer['id'],
            "timestamp": f'{current.replace(microsecond=0).isoformat()}',
            "energyUsed": used,
            "region": customer['region']
        })
        if not b:
            print("Error sending measure")
            break
        current = current + step
    return total


def change_clock(current_time: str):
    requests.get(f"{ACCOUNTING_SERVICE_URL}/admin/time/{current_time}")
    requests.get(f"{API_MEASURES}/admin/time/{current_time}")
    requests.get(f"{API_CUSTOMERS}/admin/time/{current_time}")
    requests.get(f"{API_ORCHESTRATOR_URL}/admin/time/{current_time}")
    requests.get(
        f"{ELECTRICITY_METER_REGISTER_SERVICE_URL}/admin/time/{current_time}")


def create_and_send_production_consumption(production_measure):

    res = requests.post(
        url=f'{ELECTRICITY_METER_REGISTER_SERVICE_URL}/production-measure', json=production_measure)
    if res.status_code in range(200, 299):
        return True
    else:
        print('Failed to send measure to SmartrixGrid')
        return False


def aggregate_daily(start, end):
    cur = start
    while cur < end:
        change_clock(cur.isoformat()+'Z')
        requests.get(f"{API_MEASURES}/daily-scheduler")
        cur = cur+datetime.timedelta(days=1)


def aggregate_monthly(start, end):
    cur = start
    while cur < end:
        change_clock(cur.isoformat()+'Z')
        requests.get(f"{API_MEASURES}/monthly-scheduler")
        cur = cur+datetime.timedelta(days=30)


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("nbcustomer", help="Nombre de consommateurs", type=int)
    parser.add_argument('--simulate', action="store_true",
                        help="Continuer à envoyer des measures pour simuler un consommateur")
    parser.add_argument('--history', type=int,
                        help="Sur combien d'heures il faut générer une historique pour les clients")

    args = parser.parse_args()

    for i in range(0, args.nbcustomer):
        customer = create_random_customer()
        if customer != None:
            threading.Thread(target=simulate_electric_meter,
                             args=[customer]).start()
            print('-')

            threading.Thread(target=generate_random_history, args=[
                             customer, datetime.timedelta(hours=1)]).start()
            #threading.Thread(target=simulate_electric_meter, args=[customer]).start()
