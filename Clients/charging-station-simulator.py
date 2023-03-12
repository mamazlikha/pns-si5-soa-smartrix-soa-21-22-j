
import requests
import argparse
import os
API_ORCHESTRATOR = f'http://{os.environ.get("API_ORCHESTRATOR_HOST", "localhost")}:{os.environ.get("API_ORCHESTRATOR_PORT", "8080")}'

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("stationId", help="Id de la station de recharge de véhicules électriques", type=str)
    args=parser.parse_args()


    res=requests.get(f"{API_ORCHESTRATOR}/charging-stations")

    if res.status_code >=200 and res.status_code <=299:
        charging_station=res.json()
        
    else:
        print("Cette station de recharge n'existe pas")

    



    


    
    