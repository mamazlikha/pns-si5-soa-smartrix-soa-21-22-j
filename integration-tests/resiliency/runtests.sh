#!/bin/bash

#before
docker-compose -f docker-compose.electricitysale.yml rm -svf
docker-compose -f docker-compose.electricitysale.yml up
#--abort-on-container-exit  --exit-code-from testrunner
docker container rm teamj-mongo teamjrabbitmq customerservicehost  resiliencytestrunnerhost accountinghost bankservicehost energyreservehost