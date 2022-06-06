FROM openjdk:11
MAINTAINER Kostandov Alexander

EXPOSE 8080

RUN mkdir ./app

COPY ./currency-checker-0.0.1-SNAPSHOT.jar ./app

CMD java -jar ./app/currency-checker-0.0.1-SNAPSHOT.jar