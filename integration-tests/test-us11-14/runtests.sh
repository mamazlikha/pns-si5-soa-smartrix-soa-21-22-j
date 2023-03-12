#!/bin/bash

#before

docker-compose -f docker-compose.saveproduction.yml up --abort-on-container-exit --exit-code-from testrunner

docker container rm teamj-mongo energymonitorservice customerservicehost us1114testrunnerhost localproductionmonitor
