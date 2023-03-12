Références:
- https://database.guide/mongodb-datetostring-format-specifiers/
- https://stackoverflow.com/questions/58139175/running-actions-in-another-directory#58142276
- https://stackoverflow.com/questions/43801947/bash-get-return-value-of-a-command-and-exit-with-this-value#43802810
- https://docs.mongodb.com/realm/triggers/scheduled-triggers/
- https://docs.mongodb.com/manual/meta/aggregation-quick-reference/
- https://docs.mongodb.com/manual/aggregation/
- https://docs.mongodb.com/manual/core/materialized-views/
- https://docs.mongodb.com/manual/core/timeseries-collections/
- https://docs.mongodb.com/v4.4/core/schema-validation/
- https://docs.mongodb.com/manual/reference/operator/aggregation/group/
- https://docs.mongodb.com/realm/triggers/scheduled-triggers/
- https://github.com/spring-projects/spring-data-mongodb/blob/main/src/main/asciidoc/reference/mongo-repositories-aggregation.adoc
- https://github.com/vinsguru/vinsguru-blog-code-samples/tree/65f7c068c0f9f94b48ffad22119f99d9de2fa7fe/db/reactive-mongo-aggregation
- https://vinsguru.medium.com/spring-data-reactive-mongodb-aggregation-pipeline-2597ca25cb5e
- https://database.guide/5-ways-to-get-the-hour-from-a-date-in-mongodb/
- https://www.digitalocean.com/community/tutorials/workflow-downloading-files-curl




```sh

docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.9-management

curl -X POST $API_CUSTOMERS_HOST:$API_CUSTOMERS_PORT/actuator/shutdown

curl -X POST $EMAIL_SERVICE_HOST:$EMAIL_SERVICE_PORT/actuator/shutdown

curl -X POST $ORCHESTRATOR_SERVICE_HOST:$ORCHESTRATOR_SERVICE_PORT/actuator/shutdown


management.endpoints.web.exposure.include=*
management.endpoint.shutdown.enabled=true
endpoints.shutdown.enabled=true

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

```



Il y a deux scripts pour construire les images `prepare.sh` et `prepare.local.sh`. Le premier fait la compilation des projets dans un container docker. L'avantage est qu'il est plus cross-plateforme ainsi. Le désavantage est que pour la compilation les conteneurs vont retélécharger, chaque conteneur va télécharger les dépendances