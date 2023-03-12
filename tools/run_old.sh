echo "> EXECUTING : energysupplier"
start sh energysupplier.sh

echo "> EXECUTING : customerservice"
start sh customerservice.sh

echo "> EXECUTING : EnergyOrchestratorSupplier"
start sh energyorchestratorsupplier.sh

echo "> EXECUTING : energymonitoringservice"
start sh energymonitoringservice.sh

echo "> EXECUTING : emailservice"
start sh emailservice.sh

echo "> EXECUTING : erergyreserve"
start sh erergyreserve.sh

echo "> EXECUTING : LocalProductionMonitor"
start sh electricmeterregister.sh


echo "> Please wait while services begin ..."
sleep 60

#echo "> EXECUTING : creating 5 customers"
#start sh createcustomers.sh
