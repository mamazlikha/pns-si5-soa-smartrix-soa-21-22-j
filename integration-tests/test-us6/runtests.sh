#!/bin/bash

#before

docker-compose -f docker-compose-customerdup.yml up --abort-on-container-exit --exit-code-from testrunner

docker container rm teamj-mongo teamjrabbitmq customerservicehost emailservicehost testrunnerhost supplierorchestratorhost