from datetime import datetime, timedelta

import random
import requests
import saveproduction
import duplicationofcustomertest
import customerui
import ceoui
import nonessentialcontrol
import os
import json
import time

from helper import change_clock, create_and_send_production_consumption, create_customer, generate_random_history
API_ORCHESTRATOR_URL = os.environ.get(
    "API_ORCHESTRATOR_URL", "http://localhost:8082")
API_CUSTOMERS = f'http://{os.environ.get("API_CUSTOMERS_HOST", "localhost")}:{os.environ.get("API_CUSTOMERS_PORT", "8080")}'
API_MEASURES = f"http://{os.environ.get('API_MEASURES_HOST', 'localhost')}:{os.environ.get('API_MEASURES_PORT', '5004')}"

ELECTRICITY_METER_REGISTER_SERVICE_URL = f"http://{os.environ.get('LOCAL_PRODUCTION_MONITOR_HOST', 'localhost')}:{os.environ.get('LOCAL_PRODUCTION_MONITOR_PORT', '8083')}"

def print_json(p):
    print(json.dumps(p, indent=2))

def user_story1():
    print("User story 1")
    print("En tant que 'Pierre DUBOIS' je souhaite voir ma consommation pour connaitre les jours où je consomme le plus d'énergie.")
    print("Pour cela je vais sur mon interface je renseigne mon nom et demande à voir ma consommation pour les 30 derniers jours.")
    print("....")

    change_clock("2020-12-16T10:00:00.00Z")
    result = customerui.consult_customer_bill(
        'Pierre DUBOIS', 'LAST_MONTH_BY_DAY')
    print_json(result)
    print("....\n")


def user_story3():
    print("User story 3")
    print("En tant que Charles DONNEL, CEO de Smartrix Grid je souhaite avoir une vue générale de la consommation de mes clients.")
    print("Pour cela je me connecte sur mon interface et je demande à voir la consommation mensuelle de mes clients pour ces 12 derniers mois.")
    print("....")
    change_clock("2020-12-16T10:00:00.00Z")
    result = ceoui.consult_overview('LAST_MONTH_BY_DAY')
    print_json(result)
    print("....\n")


def user_story2_and5():
    print("User stories 2 & 5")
    print("""En tant que Nikola REED, CEO de Smartrix Grid je souhaite m'assurer que la production reste toujours supérieure ou égale à la consommation

    Scénario:

    ***Etant donné que le 24 décembre 2012 à 22h00 Nikola calcule la consommation de l'ensemble des régions pour les dernières 100 secondes
    """)

    change_clock("2012-12-24T22:00:00.00Z")

    requests.get(f"{API_ORCHESTRATOR_URL}/orchestrator/compute")
    print("**** Lorsqu'il refait le même calcul à 22h15")
    change_clock("2012-12-24T22:15:00.00Z")

    print("**** Il détecte une augmentation de la consommation dans la région C, il augmente alors la production de la centrale de la région C")
    requests.get(f"{API_ORCHESTRATOR_URL}/orchestrator/compute")

    print("Waiting for the logs of the services")
    time.sleep(3)


def user_stories8_and9_and15():
    donald = create_customer('Donald McKey')
    requests.post(f"{API_CUSTOMERS}/customers/{donald['name']}/battery", json={
        'currentLevel': 0,
        'maximumCapacity': 1000,
        'customerId': donald['id']
    })
    print("Waiting 1 second ...")
    time.sleep(1)

    print("Le panneau solaire de Donald McKey commence à charger la batterie")
    print("En meme temps Donald consomme sur la batterie et sur le réseau local")
    print("Lorsque la batterie est pleine ou la dépense en un instant T est inférieure à la production alors l'excès est vendu à SmartrixGrid et envoyé à la réserve")

    total_prod = 1500
    total_consum = 250
    current = datetime(2022, 10, 29, 10, 0)

    change_clock(current.isoformat()+'Z')

    while total_prod > 0:

        tmp = random.randint(0, 100)
        prod = tmp if total_prod > tmp else total_prod
        tmp = random.randint(0, 30)
        consum = tmp if total_consum > tmp else total_consum

        total_consum -= consum
        total_prod -= prod

        create_and_send_production_consumption({
            'production': {
                'quantity': prod,
                'customerId': donald['id'],
                'moment': f'{current.replace(microsecond=0).isoformat()}',
                'region': donald['region']
            },
            'consumption': {
                "customerId": donald['id'],
                "timestamp": f'{current.replace(microsecond=0).isoformat()}',
                "energyUsed": consum,
                "region": donald['region']
            }
        })

        current = current+timedelta(seconds=1800)
    change_clock(datetime(2022, 11, 2, 10, 0).isoformat()+'Z')
    requests.get(
        f"{ELECTRICITY_METER_REGISTER_SERVICE_URL}/admin/monthly-scheduler")


def user_story10():
    change_clock("2001-10-27T18:00:00.00Z")
    print("""User story 10\nEn tant que Charles, je veux controller les consommations non-essentielles au sein d'une région, afin de réduire la consommation globale de la grille.
    Scénario :\nCharles commence par consulter la consommation actuelle de la région A. Il souhaite réduire la consommation de cette région et décide de couper les consommations 'Non-Essentiels' de cette région. De ce fait, tous les composants 'non-essentiels' des clients de cette région seront éteintes.\nIl consulte une nouvelle fois la consommation de la région, et remarque une baisse non négligeable.
        """)
    nonessentialcontrol.main()

def user_stories11_and14():

    print("user story 11 & 14 :")
    saveproduction.main()

def user_story_6():
    print("user story 6 :")
    duplicationofcustomertest.main()

def user_stories4_and7():
    station = None
    res = requests.post(f"{API_ORCHESTRATOR_URL}/chargingStations", json={
        "name": "Station Les Pins",

        "region": "A",

        "slotCount": 10,

        "energyPerSlot": 1000,

        "slotUsableCount": 3
    })

    if res.status_code != 201:
        print("Erreur d'initialisation d'une station de recharge quitting",
              res.reason, res.status_code)
        exit()
    else:
        station = res.json()

    print("""\n User stories 4 & 7
    En tant que Sarah McBean, propriétaire d'une voiture électrique je souhaite recharger mon véhicule pour pouvoir l'utiliser le lendemain.
    Scénario:
    ****Etant donné une grille surchargée à 18h.
    """)

    change_clock("2023-10-27T18:00:00.00Z")
    requests.get(f"{API_ORCHESTRATOR_URL}/orchestrator/compute")
    time.sleep(5)

    print("****Lorsque Sarah branche son véhicule")
    print("""
    La station envoie au système de SmartrixGrid une requete pour demander l'autorisation de recharger le véhicule.
    """)
    chargingRequest = {
        "chargingStationID": station["id"],
        "chargeDuration": 2,
        "energyQuantity": 1000
    }
    print("Contenu de la requete ", chargingRequest)
    autorisation = requests.post(
        f"{API_ORCHESTRATOR_URL}/plugCar", json=chargingRequest)
    print("Alors le véhicule de Sarah ne peut se recharger immédiatement")

    print(autorisation.text, autorisation.status_code)
    assert autorisation.status_code == 406

    print("**** Etant donné une grille moins chargée à 01h du matin, ")
    currentTime = "2023-10-28T01:00:00.00Z"

    change_clock(currentTime)
    requests.get(f"{API_ORCHESTRATOR_URL}/orchestrator/compute")
    time.sleep(5)

    print("""**** Lorsque la station redemande l'autorisation de recharger le véhicule de Sarah, """)
    autorisation = requests.post(
        f"{API_ORCHESTRATOR_URL}/plugCar", json=chargingRequest)
    print("Alors SmartrixGrid donne l'autorisation car la charge sur la grille a diminué")
    print("""Le véhicule de Sarah peut donc se recharger.""")
    assert autorisation.status_code == 202
    print(autorisation.text, autorisation.status_code)


def user_stories12_and13():
    current_time = "2024-08-26T22:00:00.00Z"

    change_clock(current_time)

    customer = create_customer('Morgan Ian', region='A')
    customer_name = customer["name"]

    print("Waiting   ...")
    time.sleep(2)
    print("Generating consumption history ... please waiting")
    start = datetime(2024, 9, 1)
    end = datetime(2024, 10, 17)
    customer_consumption = generate_random_history(customer, start, end)

    print(customer_consumption)
    print("  ...")
    cur = start
    while cur < end:
        requests.get(f"{API_MEASURES}/daily-scheduler")
        change_clock(cur.isoformat()+'Z')
        cur = cur+timedelta(days=1)

    # change date

    current_time = "2024-10-02T22:00:00.00Z"
    change_clock(current_time)
    # TODO mettre à jour
    res = requests.get(f"{API_MEASURES}/monthly-scheduler")

    print(" ...")
    time.sleep(4)

    assert res.status_code == 200

    res = requests.get(
        f"{API_CUSTOMERS}/customers/{customer_name}/bills/year/{2024}/month/{9}")

    assert res.status_code == 200

    bill = res.json()

    print("The customer try to get the Bill for September 2021")
    print_json(bill)

    current_time = "2024-10-18T22:00:00.00Z"
    change_clock(current_time)

    res = requests.get(
        f"{API_CUSTOMERS}/customers/{customer_name}/bills/prevision")

    print("Check previsional bill for October 2021")
    print_json(res)


def user_stories_16():
    
    print("Etant donné que SmartrixGrid a 5000 utilisateurs lorsque on calcul les factures de tous les consommateurs à la fin du mois alors")
    print("Le calcul se fait rapidement")
    st=time.time()
    change_clock(datetime(2005, 2, 10).isoformat()+'Z')
    requests.get(f"{API_MEASURES}/monthly-scheduler")
    print("***It tooks ",time.time()-st)

if __name__ == '__main__':

    res = requests.get(f'{API_ORCHESTRATOR_URL}/admin/suppliers')

    print('Suppliers list:',json.dumps(res.json(),indent=2), res.status_code)
    
    while len(res.json()) < 2:
        print("Waiting for all suppliers to register")
        print(json.dumps(res.json(),indent=2), res.status_code)
        res = requests.get(f'{API_ORCHESTRATOR_URL}/admin/suppliers')
    try:
        user_story1()
    except :
        print('User story 1 has exception')
    print("**********************************************")
    try:
        user_stories11_and14()
    except :
        print('User story 11 & 14 has exception')
    try:
        user_story_6()
    except :
        print('User story 6 has exception')
    try:
        user_story3()
    except :
        print('User story 3 has exception')
    try:
        user_story2_and5()
    except :
        print('User story 3 has exception')
    try:
        user_stories8_and9_and15()
    except :
        print('User story 3 has exception')
    try:
        user_stories4_and7()
    except :
        print('User story 4 & 7 has exception')
    try:
        user_stories12_and13()
    except :
        print('User story 12 & 13 has exception')
    try:
        user_stories_16()
    except :
        print('User story 16 has exception')
    try:
        user_story10()
    except :
        print('User story 10 has exception')
