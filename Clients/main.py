# importing the requests library
import requests
import random
import time
import argparse
import names
import datetime
import threading
import os

MAX_GENERATION = 20
# defining the api-endpoint
API = "http://" + os.environ.get('HOST', 'localhost') + ":8080/"

API_CUSTOMERS = f'http://{os.environ.get("API_CUSTOMERS_HOST", "localhost")}:{os.environ.get("API_CUSTOMERS_PORT", "8080")}/'
API_MEASURES = "http://"+os.environ.get('API_MEASURES_HOST', 'localhost')+":"+os.environ.get('API_MEASURES_PORT', '5004')+"/"
API_MEASURES_ENDPOINT = API_MEASURES+"measures"
API_CUSTOMERS_ENDPOINT = API_CUSTOMERS+"customers"


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


def create_customer(name):
    res = requests.post(url=API_CUSTOMERS_ENDPOINT, json={
        "name": name,
        "type": "PRIVATE_PERSON"
    })
    if res.status_code in range(200, 299):
        tmp = res.json()
        print('Created customer', name, 'with id', tmp["id"])
        return tmp
    else:
        print("Failed to create a random customer", res.status_code)


def create_random_customer():
    random_name = names.get_full_name()
    return create_customer(random_name)


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


def create_custommer_and_simulate(customer):
    if customer:
        threading.Thread(target=simulate_electric_meter, args=[customer]).start()

        threading.Thread(target=generate_random_history, args=[customer, datetime.timedelta(hours=1)]).start()
        # threading.Thread(target=simulate_electric_meter, args=[customer]).start()


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("nbcustomer", help="Nombre de consommateurs", type=int)
    parser.add_argument('--simulate', action="store_true",
                        help="Continuer à envoyer des measures pour simuler un consommateur")
    parser.add_argument('--history', type=int,
                        help="Sur combien d'heures il faut générer une historique pour les clients")
    parser.add_argument('--default_custommer', type=str, help="Un client qui doit forcement être créer")
    args = parser.parse_args()
    if args.default_custommer:
        create_custommer_and_simulate(create_customer(args.default_custommer))
        args.nbcustomer -= 1

    for i in range(0, args.nbcustomer):
        create_custommer_and_simulate(create_random_customer())
