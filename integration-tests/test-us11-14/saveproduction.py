import requests
import os
import names
import random
import time
import datetime
#from kafka import KafkaProducer

#producer = KafkaProducer(bootstrap_servers='localhost:9092') # Don't modify this unless you know what to do

API_CUSTOMERS = f'http://{os.environ.get("API_CUSTOMERS_HOST", "localhost")}:{os.environ.get("API_CUSTOMERS_PORT", "8080")}'
ACCOUNTING_SERVICE_URL = f"http://{os.environ.get('ACCOUNTING_SERVICE_HOST', 'localhost')}:{os.environ.get('ACCOUNTING_SERVICE_PORT', '5007')}"
API_MEASURES = f"http://{os.environ.get('ENERGY_MONITOR_HOST', 'localhost')}:{os.environ.get('ENERGY_MONITOR_PORT', '5004')}"


ELECTRICITY_METER_REGISTER_SERVICE_URL = f'http://{os.environ.get("ENERGY_METER_REGISTER_HOST", "localhost")}:{os.environ.get("ENERGY_METER_REGISTER_PORT", "8083")}'


def change_clock(current_time: str):
    requests.get(f"{API_MEASURES}/admin/time/{current_time}")
    requests.get(f"{API_CUSTOMERS}/admin/time/{current_time}")
    requests.get(f"{ELECTRICITY_METER_REGISTER_SERVICE_URL}/admin/time/{current_time}")


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




def generate_history(customer, lower: datetime.datetime, upper: datetime.datetime, region):
    """
    This method will generate random consumption history for the given customer
    """

    total = 0
    if not customer:
        return

    current = lower

    while current < upper:
        used = random.randint(0, 30)

        total += used

        b = create_and_send_production_consumption({
            'production': {
                'quantity': 500,
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

    currentTime = "2023-08-26T22:00:00.00Z"

    change_clock(currentTime)


    regions = ['A', 'B', 'C']

    region = 'A'

    customer_request = requests.post(
        url=f'{API_CUSTOMERS}/customers', json=create_random_person(region=region))

    customerId = customer_request.json()['id']

    print('saving production & consumption of customer id : ', customerId)


    generate_history(customer_request.json(), datetime.datetime(2023, 10, 1),
                     datetime.datetime(2023, 10, 17), region)


    customer_autarky_request = requests.get(
        url=f'{ELECTRICITY_METER_REGISTER_SERVICE_URL}/is-autarky/' + customerId)

    print("processing to see if customer id " +
          customerId + "is autarky or not")

    time.sleep(2)

    print(customer_autarky_request.text)

    for i in range(3):
        print('saving production & consumption of customer id : ', customerId)
        index = random.randint(0, 2)
        region = 'A'

        customer_request = requests.post(
            url=f'{API_CUSTOMERS}/customers', json=create_random_person(region=region))
        customerId = customer_request.json()['id']


        generate_history(customer_request.json(), datetime.datetime(2023, 10, 1),
                         datetime.datetime(2023, 10, 17), region)

    cur = datetime.datetime(2023, 10, 1)
    end = datetime.datetime(2023, 10, 19)

    while cur < end:
        change_clock(cur.isoformat()+'Z')
        requests.get(f"{API_MEASURES}/daily-scheduler")
        cur = cur+datetime.timedelta(days=1)

    print('checking if region A is autarky : ')
    time.sleep(2)

    change_clock(datetime.datetime(2023, 10, 17).isoformat()+'Z')

    print(requests.get(
        url=f'{ELECTRICITY_METER_REGISTER_SERVICE_URL}/is-region-autarky/' + 'A').text)

"""    print('checking if region B is autarky : ')
    time.sleep(2)
    print(requests.get(
        url=f'{ELECTRICITY_METER_REGISTER_SERVICE_URL}/is-region-autarky/' + 'B').text)

    print('checking if region C is autarky : ')
    time.sleep(2)
    print(requests.get(
        url=f'{ELECTRICITY_METER_REGISTER_SERVICE_URL}/is-region-autarky/' + 'C').text)
"""



if __name__ == '__main__':

    main()