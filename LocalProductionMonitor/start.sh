#!/bin/bash

wait-for-it -t 0 $KAFKA_SERVER_HOST:$KAFKA_SERVER_PORT
wait-for-it -t 0 $RABBIT_HOST:5672
wait-for-it -t 0 $SPRING_DATA_MONGODB_HOST:$SPRING_DATA_MONGODB_PORT

java -jar LocalProductionMonitor.jar