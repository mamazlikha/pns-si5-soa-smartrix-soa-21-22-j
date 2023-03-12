import requests
import os
import names
import random
import time
API_CUSTOMERS = f'http://{os.environ.get("API_CUSTOMERS_HOST", "localhost")}:{os.environ.get("API_CUSTOMERS_PORT", "8080")}'

EMAIL_SERVICE_URL = f'http://{os.environ.get("EMAIL_SERVICE_HOST", "localhost")}:{os.environ.get("EMAIL_SERVICE_PORT", "8085")}'

ORCHESTRATOR_SERVICE_URL=f'http://{os.environ.get("ORCHESTRATOR_SERVICE_HOST", "localhost")}:{os.environ.get("ORCHESTRATOR_SERVICE_PORT", "8082")}'


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



createdrequest = requests.post(url=f'{API_CUSTOMERS}/customers', json=create_random_person(region='A'))

customer_name=createdrequest.json()["name"]

print("Waiting few seconds before selling electricity ...")
time.sleep(3)


res=requests.post(f"{API_CUSTOMERS}/sell-electrity/{customer_name}", json={
            'quantity': 12,
            'price': 20
        }
    )
print("Waiting few seconds before exiting test ...")
time.sleep(3)