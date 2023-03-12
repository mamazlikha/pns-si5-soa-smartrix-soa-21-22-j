import requests
import os
import names
import random
import time
import datetime
from helper import change_clock
#from kafka import KafkaProducer

#producer = KafkaProducer(bootstrap_servers='localhost:9092') # Don't modify this unless you know what to do

API_CUSTOMERS = f'http://{os.environ.get("API_CUSTOMERS_HOST", "localhost")}:{os.environ.get("API_CUSTOMERS_PORT", "8080")}'
ACCOUNTING_SERVICE_URL = f"http://{os.environ.get('ACCOUNTING_SERVICE_HOST', 'localhost')}:{os.environ.get('ACCOUNTING_SERVICE_PORT', '5007')}"
API_MEASURES = f"http://{os.environ.get('ENERGY_MONITOR_HOST', 'localhost')}:{os.environ.get('ENERGY_MONITOR_PORT', '5004')}"


ELECTRICITY_METER_REGISTER_SERVICE_URL = f'http://{os.environ.get("LOCAL_PRODUCTION_MONITOR_HOST", "localhost")}:{os.environ.get("LOCAL_PRODUCTION_MONITOR_PORT", "8083")}'





def create_random_person(region='B'):
    name = names.get_full_name()
    email = name.lower().replace(' ', '.')+'@xmail.com'
    return {
        "name": name,
        "email": email,
        "region": region,
        "type": "PRIVATE_PERSON",
        "bankAccount": {
            "iban": f"PEZ{random.randint(788888889,9000000000000)}",
            "swiftCode": f"FRG{random.randint(20000,50000)}"
        }
    }




def generate_history(customer, lower: datetime.datetime, upper: datetime.datetime, region, production):
    """
    This method will generate random consumption history for the given customer
    """

    total = 0
    if not customer:
        return

    current = lower

    while current < upper:
        used = random.randint(20, 30)

        total += used

        b = create_and_send_production_consumption({
            'production': {
                'quantity': production,
                'customerId': customer['id'],
                'moment':f'{current.replace(microsecond=0).isoformat()}',
                'region': region
            },
            'consumption':{
                "customerId": customer['id'],
                "timestamp": f'{current.replace(microsecond=0).isoformat()}',
                "energyUsed": used,
                "region": customer['region']
            }
        })
        if not b:
            break
        current = current + datetime.timedelta(seconds=3600)
    return total



def create_and_send_production_consumption(production_measure):

    res = requests.post(url=f'{ELECTRICITY_METER_REGISTER_SERVICE_URL}/production-measure',json=production_measure)
    if res.status_code in range(200, 299):
        return True
    else:
        print('Failed to send measure to SmartrixGrid')
        return False


def main():

    print("we have 3 regions, A, B and C")
    currentTime = "2035-08-26T22:00:00.00Z"



    energy_quantity_productions = [50, 10, 30]

    regions = ['A', 'B', 'C']

    region = 'A'
    print("create a random person")
    random_customer_request = requests.post(
        url=f'{API_CUSTOMERS}/customers', json=create_random_person(region=region))

    cur = datetime.datetime(2035, 10, 1)
    end = datetime.datetime(2035, 10, 19)


    customerId = random_customer_request.json()['id']

    print('saving production & consumption of customer id : ', customerId)

    print("Generating history of the customer: ", random_customer_request.json()['name'])
    print("This customers lives in region A")
    print("this customer has a production capacity of 50kw/h and he has a battery.\nHe consumes between 20 - 30 kw/h")
    print("So this client is autarky, let's verify it :")
    requests.post(f"{API_CUSTOMERS}/customers/{random_customer_request.json()['name']}/battery", json={
        'currentLevel': 0,
        'maximumCapacity': 1000,
        'customerId': random_customer_request.json()['id']
    })
    generate_history(random_customer_request.json(), datetime.datetime(2035, 10, 1),
                     datetime.datetime(2035, 10, 17), region, energy_quantity_productions[0])

    time.sleep(2)

    print('please wait ...')

    i = 0
    for region in regions:
        print('saving production & consumption of customers in region ', region)
        for j in range(3): # create 3 random customers in each region

            customer_request = requests.post(
                url=f'{API_CUSTOMERS}/customers', json=create_random_person(region=region))
            customer = customer_request.json()
            requests.post(f"{API_CUSTOMERS}/customers/{customer['name']}/battery", json={
                'currentLevel': 0,
                'maximumCapacity': 1000,
                'customerId': customer['id']
            })

            generate_history(customer_request.json(), datetime.datetime(2035, 10, 1),
                             datetime.datetime(2035, 10, 17), region, energy_quantity_productions[i])
        i += 1



    cur = datetime.datetime(2035, 10, 1)
    end = datetime.datetime(2035, 10, 19)

    while cur < end:
        change_clock(cur.isoformat()+'Z')
        requests.get(f"{API_MEASURES}/daily-scheduler")
        cur = cur+datetime.timedelta(days=1)
    change_clock(datetime.datetime(2035, 10, 17).isoformat()+'Z')

    customer_autarky_request = requests.get(
        url=f'{ELECTRICITY_METER_REGISTER_SERVICE_URL}/is-autarky/' + customerId)

    print("processing to see if customer " + random_customer_request.json()['name']
           + "is autarky or not")
    print(customer_autarky_request.text)
    print("region A is autarky because each customer of this region has production capacity over 50 and they all have batteries, So region A is Autarky, let's verify it ...")
    print('checking if region A is autarky : ')
    time.sleep(2)

    print(requests.get(
        url=f'{ELECTRICITY_METER_REGISTER_SERVICE_URL}/is-region-autarky/' + 'A').text)
    print("All customers in region B has a production capacity of 10 kw/h and they have batteries, and the consume around 20 - 30 kW/h, so region B is not Autarky")
    print('checking if region B is autarky : ')
    time.sleep(2)
    print(requests.get(
        url=f'{ELECTRICITY_METER_REGISTER_SERVICE_URL}/is-region-autarky/' + 'B').text)

    print("Each customer in region C has a production capacity of 30 kw/h and each customer consummes around 20 - 30 kw/h and has battery, so region C is Autarky")
    print('checking if region C is autarky : ')
    time.sleep(2)
    print(requests.get(
        url=f'{ELECTRICITY_METER_REGISTER_SERVICE_URL}/is-region-autarky/' + 'C').text)



if __name__ == '__main__':

    main()