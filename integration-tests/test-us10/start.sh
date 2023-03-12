#!/bin/bash

until [ $(curl --silent $API_CUSTOMERS_HOST:$API_CUSTOMERS_PORT/actuator/health | grep UP -c ) == 1 ]
do
  echo "Customer Service Not Ready"
  sleep 1
done

until [ $(curl --silent $ENERGY_MONITOR_HOST:$ENERGY_MONITOR_PORT/actuator/health | grep UP -c ) == 1 ]
do
  echo "ENERGY MONITOR Service Not Ready"
  sleep 1
done


python3 nonessentialcontrol.py

echo "****End of testing ******"


