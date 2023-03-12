import requests
import os
import names
import random
import time


API_CUSTOMERS = f'http://{os.environ.get("API_CUSTOMERS_HOST", "localhost")}:{os.environ.get("API_CUSTOMERS_PORT", "8080")}'

EMAIL_SERVICE_URL = f'http://{os.environ.get("EMAIL_SERVICE_HOST", "localhost")}:{os.environ.get("EMAIL_SERVICE_PORT", "8085")}'

ORCHESTRATOR_SERVICE_URL=f'http://{os.environ.get("ORCHESTRATOR_SERVICE_HOST", "localhost")}:{os.environ.get("ORCHESTRATOR_SERVICE_PORT", "8082")}'

import docker

client = docker.from_env()
def stopContainer(container_name):
    print("stopping...",container_name)
    container = client.containers.get("bankservicehost")
    print("container id:", container.id)
    container.stop()
    return container


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

print("Waiting few seconds before selling electricity while bank is accesible...")
time.sleep(3)


res=requests.post(f"{API_CUSTOMERS}/sell-electrity/{customer_name}", json={
            'quantity': 12,
            'price': 20
        }
    )

print("Waiting few seconds before shutdown bank...")
time.sleep(3)


print("Shudowning the bankservicehost container...")
#bank_host_container = stopContainer("bankservicehost")
bank_host_container = client.containers.get("bankservicehost")
bank_host_container.pause()

print("Waiting few seconds before sell new electricity while bank is down..")
time.sleep(3)
selled_amount = 45

print("Selling electricity of amount: ",selled_amount)
res=requests.post(f"{API_CUSTOMERS}/sell-electrity/{customer_name}", json={
    'quantity': selled_amount-10,
    'price': selled_amount
}
                  )


print("Waiting few seconds before restart bank conatainer..")
time.sleep(3)
print("Restarting bank container")

bank_host_container.unpause()
selled_amount = 75

print("Waiting few seconds after the restart of the bank conatiner..")
time.sleep(7)
print("Selling electricity of amount: ",selled_amount)
res=requests.post(f"{API_CUSTOMERS}/sell-electrity/{customer_name}", json={
    'quantity': selled_amount-10,
    'price': selled_amount
}
                  )


print("Waiting few seconds before exiting test ...")
time.sleep(3)

client = docker.from_env()
for container in client.containers.list():
    if container.name != "resiliencytestrunnerhost": 
        print("stopping container: ",container.name)
        container.stop()