FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_O

# add directly the war
ADD *.war /app.war
#ADD waiting.sh /waiting.sh
EXPOSE 8080
CMD
#chmod a+x waiting.sh && \
#    ./waiting.sh && \
    echo "The application will start in ${JHIPSTER_SLEEP}s..." && \
    sleep ${JHIPSTER_SLEEP} && \
    java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.war
