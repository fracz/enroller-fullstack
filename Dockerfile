FROM maven:3.9-amazoncorretto-17 as build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN mvn package

FROM amazoncorretto:17

ARG JAR_FILE=/usr/app/target/enroller-0.0.1-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} app.jar
ADD enroller.db enroller.db
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
