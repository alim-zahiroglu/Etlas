FROM amd64/maven:3.8.6-openjdk-18-slim
WORKDIR usr/app
COPY  .  .
ENTRYPOINT ["mvn","spring-boot:run"]
#
#FROM openjdk:17-jdk
#
#COPY ./target/deploy-demo.jar /app/etlas.jar
#COPY ./target/classes/application.yml /app/src/main/resources/application.yml
#
#EXPOSE 8080
#
#WORKDIR /app
#
#CMD ["java","-jar","etlas.jar"]
