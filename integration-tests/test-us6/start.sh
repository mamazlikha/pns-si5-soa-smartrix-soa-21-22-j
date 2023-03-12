#!/bin/bash

until [ $(curl --silent $EMAIL_SERVICE_HOST:$EMAIL_SERVICE_PORT/actuator/health | grep UP -c ) == 1 ]
do
  echo "Email Service Not Ready"
  sleep 1
done

until [ $(curl --silent $API_CUSTOMERS_HOST:$API_CUSTOMERS_PORT/actuator/health | grep UP -c ) == 1 ]
do
  echo "Customer Service Not Ready"
  sleep 1
done

until [ $(curl --silent $ORCHESTRATOR_SERVICE_HOST:$ORCHESTRATOR_SERVICE_PORT/actuator/health | grep UP -c ) == 1 ]
do
  echo "Orchestrator Service Not Ready"
  sleep 1
done



python3 main.py

ret_code=$?
echo "****End of testing ******"
exit $ret_code

