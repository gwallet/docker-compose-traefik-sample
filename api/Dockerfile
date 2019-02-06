FROM openjdk:8-jre-alpine

COPY target/ip-api-*.jar /opt/app.jar

ENV PROD_MODE=true
ENV LISTEN_PORT=8080

CMD /usr/bin/java -jar /opt/app.jar
