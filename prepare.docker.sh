#!/bin/bash

cd LocalProductionMonitor
docker build -t teamj-soa-local-production-monitor-service .
cd ..

cd CustomerService
docker build  -t teamj-soa-customer-service .
cd ..

cd emailservice
docker build  -t teamj-soa-emailservice .
cd ..

cd SupplierOrchestrator
docker build  -t teamj-soa-orchestrator .
cd ..

cd AccountingService
docker build  -t teamj-soa-accounting .
cd ..

#La banque
cd bank
docker build  -t teamj-soa-bankservice .
cd ..

cd reserveenergy
docker build  -t teamj-soa-energyreserve .
cd ..

cd energymonitorservice
docker build  -t teamj-soa-energy-monitor-service .
cd ..

cd EnergySupplier
docker build -t teamj-soa-energy-supplier-service .
cd ..

cd demo
docker build -t teamj-soa-runner .
docker build -f Dockerfile.data.generation -t teamj-soa-data-generator .
cd ..
