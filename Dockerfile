# Start with a base image containing Java runtime

FROM openjdk:8-jdk-alpine

LABEL maintainer="samucapuc@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=/target/encoding-video-test-sambatech.jar

# Add the application's jar to the container
ADD ${JAR_FILE} encoding-video-test-sambatech.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-Djava.security.egd=file:/dev/./urandom","-jar","/encoding-video-test-sambatech.jar"]
