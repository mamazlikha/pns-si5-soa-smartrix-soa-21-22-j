#!/bin/bash

until [ $(curl --silent $ORCHESTRATOR_SERVICE_HOST:$ORCHESTRATOR_SERVICE_PORT/actuator/health | grep UP -c ) == 1 ]
do
  echo "Orchestrator Service Not Ready"
  sleep 2
done


until [ $(curl --silent $API_CUSTOMERS_HOST:$API_CUSTOMERS_PORT/actuator/health | grep UP -c ) == 1 ]
do
  echo "Customer Service Not Ready"
  sleep 1
done

until [ $(curl --silent $ENERGY_MONITOR_HOST:$ENERGY_MONITOR_PORT/actuator/health | grep UP -c ) == 1 ]
do
  echo "Monitor Service Not Ready"
  sleep 1
done

until [ $(curl --silent $EMAIL_SERVICE_HOST:$EMAIL_SERVICE_PORT/actuator/health | grep UP -c ) == 1 ]
do
  echo "Email Service Not Ready"
  sleep 1
done

until [ $(curl --silent $LOCAL_PRODUCTION_MONITOR_HOST:$LOCAL_PRODUCTION_MONITOR_PORT/actuator/health | grep UP -c ) == 1 ]
do
  echo "LOCAL PRODUCTION MONITOR service Not Ready"
  sleep 1
done

echo "Unzipping databases-dump.zip "
unzip /demodata/databases-dump.zip -d /demodata/databases
rm -r /demodata/databases/jydrodb
rm -r /demodata/databases/jcoaldb
mongorestore --host="$MONGO_HOST:$MONGO_PORT" /demodata/databases

python3 initialize.py
echo "Waiting ..."
sleep 10
python3 demo.py


