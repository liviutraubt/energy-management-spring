FROM maven:3.9 AS build

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /user_service/pom.xml
WORKDIR /user_service
RUN mvn dependency:go-offline
COPY ./src/ /user_service/src
WORKDIR /user_service
RUN mvn clean install -DskipTests

FROM openjdk:21-jdk
WORKDIR /user_service
COPY --from=build /user_service/target/*.jar /user_service/user_service.jar
EXPOSE 8080
CMD ["java", "-jar", "user_service.jar"]
