#!/bin/bash

#before

docker-compose -f docker-compose.electricitysale.yml up --abort-on-container-exit --exit-code-from testrunner

docker container rm teamj-mongo teamjrabbitmq customerservicehost  us9testrunnerhost accountinghost bankservicehost energyreservehost