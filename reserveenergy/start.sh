#!/bin/bash

echo "Displaying KAFKA_SERVER HOST"
echo $KAFKA_SERVER_HOST

wait-for-it -t 0 $KAFKA_SERVER_HOST:$KAFKA_SERVER_PORT
wait-for-it -t 0 $RABBIT_HOST:5672
java -jar reserveenergy.jar