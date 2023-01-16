FROM maven:3.8.6-openjdk-18-slim

ADD target/tournament-svc.jar tournament-svc.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/tournament-svc.jar"]
