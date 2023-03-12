#!/bin/bash

docker-compose -f docker-compose-data.yml up   --abort-on-container-exit --exit-code-from datagenerator

ret_code=$?




docker container rm --force $(docker container ls -a --quiet)



echo "**** The End ******"
exit $ret_code
