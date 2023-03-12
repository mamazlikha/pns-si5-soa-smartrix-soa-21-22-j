#!/bin/bash

cd ../CustomerService
docker build  -t teamj-soa-customer-service .

cd ../emailservice
docker build  -t teamj-soa-emailservice .

cd ../SupplierOrchestrator

docker build  -t teamj-soa-orchestrator .


cd ../AccountingService
docker build  -t teamj-soa-accounting .

#La banque
cd ../bank
docker build  -t teamj-soa-bankservice .

cd ../reserveenergy
docker build  -t teamj-soa-energyreserve .


cd ../energymonitorservice
docker build  -t teamj-soa-energy-monitor-service .


cd ../integration-tests/test-us6
docker build -f dupcustomer.dockerfile -t teamj-soa-testrunner .

cd ../test-us9
docker build -f electricitysale.dockerfile -t teamj-soa-us9-runner .

cd ../test-us12
docker build -f monthlybill.dockerfile -t teamj-soa-us12-runner .


