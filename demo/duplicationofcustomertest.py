import requests
import os
import names
import random
API_CUSTOMERS = f'http://{os.environ.get("API_CUSTOMERS_HOST", "localhost")}:{os.environ.get("API_CUSTOMERS_PORT", "8080")}'

EMAIL_SERVICE_URL = f'http://{os.environ.get("EMAIL_SERVICE_HOST", "localhost")}:{os.environ.get("EMAIL_SERVICE_PORT", "8085")}'

ORCHESTRATOR_SERVICE_URL = f'http://{os.environ.get("ORCHESTRATOR_SERVICE_HOST", "localhost")}:{os.environ.get("ORCHESTRATOR_SERVICE_PORT", "4200")}'


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


def main():
    print("En tant que Nikola, je souhaite je veux garantir qu’il n’y a jamais de pic de consommation trop élevé dans une région pour éviter qu’une partie de la grille soit surchargée.")
    print("Nikola consulte la consommation actuelle de la région ‘A’. Nikola réalise que la consommation de cette région est élevée et qu’elle est notamment sur une tendance grandissante. Souhaitant de pas surcharger cette région, il décide d’avertir tous les habitants de cette région par mail qu’une baisse en consommation est nécessaire")

    for _ in range(0, 6):
        requests.post(
            url=f'{API_CUSTOMERS}/customers', json=create_random_person(region='A'))

    for _ in range(0, 5):
        requests.post(
            url=f'{API_CUSTOMERS}/customers', json=create_random_person(region='B'))

    res=requests.get(url=f'{ORCHESTRATOR_SERVICE_URL}/regions/A/communication')

    try:
        assert res.status_code==200
    except AssertionError as e:
        print("Test failed ")
        print(e)
        exit(1)

main()