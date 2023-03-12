#!/bin/bash

set -e


function download_jdk_mvn(){
    mkdir compil
    
    curl -o compil/openjdk17.tar.gz https://download.java.net/java/GA/jdk17.0.1/2a2082e5a09d4267845be086888add4f/12/GPL/openjdk-17.0.1_linux-x64_bin.tar.gz
    
    tar -xzf compil/openjdk17.tar.gz -C compil
    
    
    curl -o compil/mvn-3.8.3.tar.gz https://dlcdn.apache.org/maven/maven-3/3.8.3/binaries/apache-maven-3.8.3-bin.tar.gz
    tar -xzf compil/mvn-3.8.3.tar.gz -C compil
    
    
}
if [ ! -d "compil" ]; then
    download_jdk_mvn
fi

export JAVA_HOME=$(pwd)/compil/jdk-17.0.1
mvn17=$(pwd)/compil/apache-maven-3.8.3/bin/mvn

cd LocalProductionMonitor
$mvn17 clean package -DskipTests
docker build -f Dockerfile.local -t teamj-soa-local-production-monitor-service .
cd ..


cd CustomerService
$mvn17 clean package -DskipTests
docker build -f Dockerfile.local -t teamj-soa-customer-service .
cd ..

cd emailservice
$mvn17 clean package -DskipTests
docker build  -f Dockerfile.local -t teamj-soa-emailservice .
cd ..

cd SupplierOrchestrator
$mvn17 clean package -DskipTests
docker build -f Dockerfile.local -t teamj-soa-orchestrator .
cd ..

cd AccountingService
$mvn17 clean package -DskipTests
docker build -f Dockerfile.local -t teamj-soa-accounting .
cd ..

#La banque
cd bank
$mvn17 clean package -DskipTests
docker build -f Dockerfile.local -t teamj-soa-bankservice .
cd ..

cd reserveenergy
$mvn17 clean package -DskipTests
docker build -f Dockerfile.local -t teamj-soa-energyreserve .
cd ..

cd energymonitorservice
$mvn17 clean package -DskipTests
docker build -f Dockerfile.local  -t teamj-soa-energy-monitor-service .
cd ..

cd EnergySupplier
$mvn17 clean package -DskipTests
docker build -f Dockerfile.local -t teamj-soa-energy-supplier-service .
cd ..

cd demo
docker build -t teamj-soa-runner .
#docker build -f Dockerfile.data.generation -t teamj-soa-data-generator .
cd ..


