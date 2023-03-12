# the first stage of our build will use a maven parent image
FROM maven:3.8.3-openjdk-17 AS MAVEN_BUILD

# copy the pom and src code to the container
COPY ./ ./

EXPOSE 5006

# package our application code
RUN mvn clean package -DskipTests

# the second stage of our build
FROM openjdk:17-bullseye

# copy only the artifacts we need from the first stage and discard the rest
COPY --from=MAVEN_BUILD ./target/reserveenergy-0.0.1-SNAPSHOT.jar ./reserveenergy.jar

COPY start.sh start.sh
RUN chmod +x start.sh
# set the startup command to execute the jar
ENTRYPOINT [ "./start.sh" ]

#HEALTHCHECK --start-period=30s --interval=30s --timeout=3s --retries=3 \
#            CMD m 5 --silent --fail --request GET http://localhost:8081/actuator/health | grep -Po '"status": *\K"[^"]*"'| { read test; if [ $test == "\"UP\"" ]; then echo true; else echo false; fi } > /dev/null || exit 1
