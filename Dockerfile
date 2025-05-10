FROM node:22.15.0-alpine AS frontend
ADD . /usr/app
WORKDIR /usr/app/src/main/frontend
RUN npm install && npm run build && rm -fr node_modules

FROM maven:3.9-amazoncorretto-17 AS build
COPY --from=frontend /usr/app /usr/app
WORKDIR /usr/app
RUN mvn package

FROM amazoncorretto:17
COPY --from=build /usr/app/target/enroller-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
