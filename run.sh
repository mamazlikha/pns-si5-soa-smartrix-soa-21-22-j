#!/bin/bash

docker-compose up  --force-recreate --abort-on-container-exit --exit-code-from testrunner

ret_code=$?


#docker container rm teamj-mongo teamjrabbitmq customerservicehost  localproductionmonitor  testrunnerhost accountinghost bankservicehost energymonitorservice emailservicehost teamjzookeeper teamjkafka teamj-jydrosupplier-host teamj-jcoalsupplier-host

docker container rm --force $(docker container ls -a --quiet)

docker volume rm $(docker volume ls --quiet)

echo "**** The End ******"
exit $ret_code
