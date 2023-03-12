#!/bin/bash

#before

docker-compose -f docker-compose.nonessentialcontrol.yml up --abort-on-container-exit --exit-code-from testrunner

docker container rm teamj-mongo teamjrabbitmq customerservicehost  us10testrunnerhost energymonitorservice