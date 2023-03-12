import requests
import os
import names
import random
import time
import json

API_CUSTOMERS = f'http://{os.environ.get("API_CUSTOMERS_HOST", "localhost")}:{os.environ.get("API_CUSTOMERS_PORT", "8080")}'
API_CUSTOMERS_ENDPOINT = API_CUSTOMERS+"/customers"

ENERGY_MONITOR=f'http://{os.environ.get("ENERGY_MONITOR_HOST", "localhost")}:{os.environ.get("ENERGY_MONITOR_PORT", "5004")}'
ENERGY_MONITOR_COMPONENT_SWITCH_ENDPOINT = ENERGY_MONITOR + "/switchCustomerComponent"
ENERGY_MONITOR_SEND_MEASURES_ENDPOINT = ENERGY_MONITOR + "/customerSendMeasures"
ENERGY_MONITOR_REGION_SWITCH_ENDPOINT = ENERGY_MONITOR + "/switchusagetype"
ENERGY_MONITOR_REGION_CONSUMPTION_ENDPOINT = ENERGY_MONITOR + "/regionTotalConsumption"

components = ["Essential Lights", "Non Essential Lights", "Car Charger", "Home appliance"]
customers = []

def create_customer(name, region):
    res = requests.post(url=API_CUSTOMERS_ENDPOINT, json={
        "name": name,
        "type": "PRIVATE_PERSON",
        "bankAccount": {
            "iban": "iban",
            "swiftCode": "swiftcode"
        },
        "region": region
    })
    if res.status_code in range(200, 299):
        tmp = res.json()
        print('Created customer', name, 'with id', tmp["id"])
        return tmp
    else:
        print("Failed to create a random customer", res.status_code)

def get_region_total_consumption(region):
    query = ENERGY_MONITOR_REGION_CONSUMPTION_ENDPOINT + '/' + region
    res = requests.get(query)
    return int(requests.get(query).text)


def switch_region_consumptions(region, on):
    state = "SWITCH_ALL_CONSUMPTION"
    if(on is False):
        state = "SWITCH_ONLY_ESSENTIAL"
    res = requests.post(url=ENERGY_MONITOR_REGION_SWITCH_ENDPOINT, json={
        "region": region,
        "usageSwitch": state
    })

def create_random_customer(region):
    random_name = names.get_full_name()
    return create_customer(random_name, region)

def switchCustomerComponents(name):
    for componentName in components:
        res = requests.post(url=ENERGY_MONITOR_COMPONENT_SWITCH_ENDPOINT, json={
            "customerName" : name,
            "componentName" : componentName,
            "switchOn" : True
        })

def init_customers():
    for i in range(0, 2):
        customer = create_random_customer("A")
        customers.append(customer)
        switchCustomerComponents(customer['name'])

def send_customer_measures(customerName):
    query = ENERGY_MONITOR_SEND_MEASURES_ENDPOINT + '/' + customerName
    requests.get(query)

def main():
    switch_region_consumptions('A', True)

    consumptionBeginning = get_region_total_consumption('A')
    consumptionMidExperience = 0
    consumptionEndExperience = 0

    print("Consumption of region A [Beginning of experience] : ", consumptionBeginning)
    print("...")

    print("Creating consumers in region A and switching all their components 'ON'...")
    init_customers()
    for c in customers:
        switchCustomerComponents(c['name'])

    print("Users send measures for their used components, 3 times.")

    for index in range(0,3):
        for c in customers:
            send_customer_measures(c['name'])

    consumptionMidExperience = get_region_total_consumption('A')
    print("...")
    print("New consumption of region A [Pre 'non-essentials' off] : ", consumptionMidExperience)
    print("Delta value of before/after consumption : ", consumptionMidExperience - consumptionBeginning)

    print("...")
    print("Charles switchs off non essential consumptions for region 'A'...")
    switch_region_consumptions('A', False)

    print("Users send new measures for their used components, 3 times.")

    for index in range(0,3):
        for c in customers:
            send_customer_measures(c['name'])

    consumptionEndExperience = get_region_total_consumption('A')

    print("...")
    print("New consumption of region A [Post 'non essentials' off] : ", consumptionEndExperience)
    print("Delta value of before/after consumption : ", consumptionEndExperience - consumptionMidExperience)





if __name__ == '__main__':

    main()