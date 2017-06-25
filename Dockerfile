FROM ubuntu:16.04
#openjdk:8-jre-alpineopenjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS=""

# add directly the war
COPY waiting.sh /waiting.sh
ADD *.war /app.war

RUN apt-get update
RUN apt-get install default-jdk -y
# openjdk version >= "1.8.0_131"
RUN apt-get install curl -y

EXPOSE 8080
CMD echo "The application is waiting for the database" && \
    chmod a+x /waiting.sh && \
    ./waiting.sh && \
    echo "The application will start in ${JHIPSTER_SLEEP}s..." && \
    sleep ${JHIPSTER_SLEEP} && \
    java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.war


