#!/bin/bash

until [ $(curl --silent $BANK_SERVICE_HOST:$BANK_SERVICE_PORT/actuator/health | grep UP -c ) == 1 ]
do
  echo "Bank Service Not Ready"
  sleep 1
done

until [ $(curl --silent $API_CUSTOMERS_HOST:$API_CUSTOMERS_PORT/actuator/health | grep UP -c ) == 1 ]
do
  echo "Customer Service Not Ready"
  sleep 1
done

until [ $(curl --silent $ACCOUNTING_SERVICE_HOST:$ACCOUNTING_SERVICE_PORT/actuator/health | grep UP -c ) == 1 ]
do
  echo "ACCOUNTING Service Not Ready"
  sleep 1
done

python3 main.py

ret_code=$?
echo "****End of testing ******"
exit $ret_code




