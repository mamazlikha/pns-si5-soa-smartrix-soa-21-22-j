#!/bin/bash

wait-for-it -t 0 $RABBIT_HOST:5672
java -jar accountingservice.jar