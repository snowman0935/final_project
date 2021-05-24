FROM openjdk:8
MAINTAINER Sreevardhan  seethana.sreevardhan@iiitb.org
COPY ./target/Integrator-1.0-SNAPSHOT-jar-with-dependencies.jar ./
WORKDIR ./
CMD ["java", "-jar", "Integrator-1.0-SNAPSHOT-jar-with-dependencies.jar"]