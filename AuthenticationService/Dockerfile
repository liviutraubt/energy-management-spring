FROM maven:3.9 AS build

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /authentication_service/pom.xml
WORKDIR /authentication_service
RUN mvn dependency:go-offline
COPY ./src/ /authentication_service/src
WORKDIR /authentication_service
RUN mvn clean install -DskipTests

FROM openjdk:21-jdk
WORKDIR /authentication_service
COPY --from=build /authentication_service/target/*.jar /authentication_service/authentication_service.jar
EXPOSE 8082
CMD ["java", "-jar", "authentication_service.jar"]
