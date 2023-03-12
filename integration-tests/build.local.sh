#!/bin/bash
set -e
#mvn17=C:/Users/user/Downloads/apache-maven-3.8.3-bin/apache-maven-3.8.3/bin/mvn
#mvn17=/home/anagonou/apache-maven-3.8.3/bin/mvn
#export JAVA_HOME=/home/anagonou/jdk-17

cd ../LocalProductionMonitor
mvn clean package -DskipTests
docker build -f Dockerfile.local -t teamj-soa-electric-metere-register-service .

cd ../CustomerService
mvn clean package -DskipTests

docker build -f Dockerfile.local -t teamj-soa-customer-service-test .
cd ../emailservice
mvn clean package -DskipTests
docker build -f Dockerfile.local -t teamj-soa-emailservice-test .

cd ../SupplierOrchestrator
mvn clean package -DskipTests
docker build -f Dockerfile.local -t teamj-soa-orchestrator-test .


cd ../AccountingService
mvn clean package -DskipTests
docker build -f Dockerfile.local -t teamj-soa-accounting-test .

#La banque
cd ../bank
mvn clean package -DskipTests
docker build -f Dockerfile.local -t teamj-soa-bankservice-test .

cd ../reserveenergy
mvn clean package -DskipTests
docker build -f Dockerfile.local -t teamj-soa-energyreserve-test .


cd ../energymonitorservice
mvn clean package -DskipTests
docker build -f Dockerfile.local -t teamj-soa-energy-monitor-service .


cd ../integration-tests/test-us6
docker build -f dupcustomer.dockerfile -t teamj-soa-testrunner .

cd ../test-us9
docker build -f electricitysale.dockerfile -t teamj-soa-us9-runner .

cd ../resiliency
docker build -f electricitysale.dockerfile -t teamj-soa-resiliency-runner .

cd ../test-us12
docker build -f monthlybill.dockerfile -t teamj-soa-us12-runner .

cd ../test-us11-14
docker build -f saveproduction.dockerfile -t teamj-soa-us11-us14-runner .


cd ../test-us10
docker build -f nonessentialcontrol.dockerfile -t teamj-soa-us10-runner .
