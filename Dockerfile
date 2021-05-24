FROM openjdk:8
MAINTAINER Sreevardhan  seethana.sreevardhan@iiitb.org
COPY ./target/SPE_final_project-1.0-SNAPSHOT-jar-with-dependencies.jar ./
WORKDIR ./
CMD ["java", "-jar", "SPE_final_project-1.0-SNAPSHOT-jar-with-dependencies.jar"]