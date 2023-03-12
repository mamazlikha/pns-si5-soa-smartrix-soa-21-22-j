#!/bin/bash

#before

docker-compose -f docker-compose.monthlybill.yml up --abort-on-container-exit --exit-code-from testrunner

docker container rm teamj-mongo teamjrabbitmq customerservicehost  us12testrunnerhost accountinghost bankservicehost energymonitorservice