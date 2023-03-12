import datetime
import requests
import os
import names
import random
import time
API_CUSTOMERS = f'http://{os.environ.get("API_CUSTOMERS_HOST", "localhost")}:{os.environ.get("API_CUSTOMERS_PORT", "8080")}'

EMAIL_SERVICE_URL = f'http://{os.environ.get("EMAIL_SERVICE_HOST", "localhost")}:{os.environ.get("EMAIL_SERVICE_PORT", "8085")}'

ORCHESTRATOR_SERVICE_URL = f'http://{os.environ.get("ORCHESTRATOR_SERVICE_HOST", "localhost")}:{os.environ.get("ORCHESTRATOR_SERVICE_PORT", "8082")}'
API_MEASURES = f"http://{os.environ.get('ENERGY_MONITOR_HOST', 'localhost')}:{os.environ.get('ENERGY_MONITOR_PORT', '5004')}"

ACCOUNTING_SERVICE_URL = f"http://{os.environ.get('ACCOUNTING_SERVICE_HOST', 'localhost')}:{os.environ.get('ACCOUNTING_SERVICE_PORT', '5007')}"


def change_clock(current_time: str):
    requests.get(f"{ACCOUNTING_SERVICE_URL}/admin/time/{current_time}")
    requests.get(f"{API_MEASURES}/admin/time/{current_time}")
    requests.get(f"{API_CUSTOMERS}/admin/time/{current_time}")


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


def send_measure(measure: dict):
    res = requests.post(url=f'{API_MEASURES}/measures', json=measure)
    if res.status_code in range(200, 299):
        return True
    else:
        print('Failed to send measure to SmartrixGrid')
        return False


def generate_history(customer, lower: datetime.datetime, upper: datetime.datetime):
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
            break
        current = current + datetime.timedelta(seconds=300)
    return total




def user_story12():
    current_time = "2021-08-26T22:00:00.00Z"

    change_clock(current_time)
    createdrequest = requests.post(
        url=f'{API_CUSTOMERS}/customers', json=create_random_person(region='A'))

    customer = createdrequest.json()
    customer_name = customer["name"]

    print("Waiting   ...")
    time.sleep(2)
    print("Generating consumption history ...")
    start=datetime.datetime(2021, 9, 1)
    end=datetime.datetime(2021, 10, 17)
    customer_consumption=generate_history(customer,start ,end)

    print(customer_consumption)
    print("  ...")
    cur=start
    while cur < end:
        requests.get(f"{API_MEASURES}/daily-scheduler")
        change_clock(cur.isoformat()+'Z')
        cur = cur+datetime.timedelta(days=1)


    # change date

    current_time = "2021-10-02T22:00:00.00Z"
    change_clock(current_time)
    # TODO mettre à jour
    res = requests.get(f"{API_MEASURES}/monthly-scheduler")

    print(" ...")
    time.sleep(4)

    assert res.status_code == 200

    res = requests.get(
        f"{API_CUSTOMERS}/customers/{customer_name}/bills/year/{2021}/month/{9}")

    assert res.status_code == 200

    bill=res.json()

    print("The customer try to get the Bill for September 2021")
    print(bill)

    current_time = "2021-10-18T22:00:00.00Z"
    change_clock(current_time)

    res = requests.get(
        f"{API_CUSTOMERS}/customers/{customer_name}/bills/prevision")

    print("Check previsional bill for October 2021")
    print(res.json())


if __name__=='__main__':
    user_story12()