mvn clean package
export SUPPLIER_DB_NAME=jydrodb
export SUPPLIER_NAME=Jydro
export SUPPLIER_CAPACITY=20000
export SUPPLIER_REGION=A

java -jar target/energysupplier-0.0.1-SNAPSHOT.jar