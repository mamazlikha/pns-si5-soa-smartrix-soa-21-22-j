#!/bin/bash


#until [ $(curl --silent $API_ORCHESTRATOR_URL/actuator/health | grep UP -c ) == 1 ]
#do
#  echo "Not Ready"
#  sleep 1
#done

java -jar bank.jar


