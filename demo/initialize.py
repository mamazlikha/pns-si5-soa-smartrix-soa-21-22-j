import os
from random import randint
import requests
import helper
import datetime
import threading

API_CUSTOMERS = f'http://{os.environ.get("API_CUSTOMERS_HOST", "localhost")}:{os.environ.get("API_CUSTOMERS_PORT", "8080")}'
API_CUSTOMERS_ENDPOINT = API_CUSTOMERS+"/customers"
API_MEASURES = f"http://{os.environ.get('API_MEASURES_HOST', 'localhost')}:{os.environ.get('API_MEASURES_PORT', '5004')}"
API_MEASURES_ENDPOINT = API_MEASURES+'/measures'


def send_measure(measure: dict):
    res = requests.post(url=API_MEASURES_ENDPOINT, json=measure)
    if res.status_code in range(200, 299):
        return True
    else:
        print('Failed to send measure to SmartrixGrid')
        return False


def saturate_region(region_name, max_capacity: int, around_time):
    sat_level = 0.9*max_capacity
    energy_used = sat_level//10
    for _ in range(0, 10):
        send_measure(
            {
                "customerId": hex(randint(1_000_000, 900_000_000))[2:],
                "timestamp": around_time,
                "energyUsed": energy_used,
                "region": region_name
            }
        )


def minimum_usage(region_name, max_capacity, around_time):
    min_level = 0.1*max_capacity
    energy_used = min_level//10
    for _ in range(0, 10):
        send_measure(
            {
                "customerId": hex(randint(1_000_000, 900_000_000))[2:],
                "timestamp": around_time,
                "energyUsed": energy_used,
                "region": region_name
            }
        )





def init_us25():
    # t1 consommation=3000
    energy_used = 3000/10
    current_time = "2012-12-24T21:59:50"
    for _ in range(0, 10):
        send_measure(
            {
                "customerId": hex(randint(1_000_000, 900_000_000))[2:],
                "timestamp": current_time,
                "energyUsed": energy_used,
                "region": "C"
            }
        )

    # t2 consommation = 6000
    energy_used = 6000/20
    current_time = "2012-12-24T22:14:50"
    for _ in range(0, 20):
        send_measure(
            {
                "customerId": hex(randint(1_000_000, 900_000_000))[2:],
                "timestamp": current_time,
                "energyUsed": energy_used,
                "region": "C"
            }
        )


def init_us4_and7():
    saturate_region('A', 10_000,'2023-10-27T17:59:45')
    minimum_usage('A', 10_000,'2023-10-28T00:59:55') #simuler la consommation à 01h du matin
    

def init_us1_and_us3():
    pierre = helper.create_customer('Pierre DUBOIS')
    marie = helper.create_customer('Marie MIDDLE')
    helper.generate_random_history(pierre, datetime.datetime(
        2020, 11, 10, 1, 0, 0), datetime.datetime(2020, 12, 15, 0, 0, 0), step=datetime.timedelta(seconds=3555))

    helper.generate_random_history(marie, datetime.datetime(
        2020, 11, 10, 1, 0, 0), datetime.datetime(2020, 12, 15, 0, 0, 0), step=datetime.timedelta(seconds=3555))

    helper.aggregate_daily(datetime.datetime(
        2020, 11, 10, 1, 0, 0), datetime.datetime(2020, 12, 17, 0, 0, 0))

    


def main():
    init_us1_and_us3()
    init_us4_and7()
    init_us25()
    print('Initialization finished')


if __name__ == '__main__':
    main()
