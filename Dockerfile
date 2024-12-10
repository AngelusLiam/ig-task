FROM eclipse-temurin:17-jdk-alpine

EXPOSE 8080

# Environment variables
ENV APP_NAME=ig-demo
ENV APP_HOME_DIR=/opt/${APP_NAME}
ENV APP_LOG_DIR=/var/log/${APP_NAME}

##### Build

# Upgrade and install system packages
RUN apk --no-cache -U upgrade && apk --no-cache add curl

# Add user and group
RUN addgroup -g 2000 -S spring
RUN adduser -G spring -S -u 2000 spring

# Create a directory for the application
RUN mkdir -p ${APP_HOME_DIR}

# Create a directory for the logs
RUN mkdir -p ${APP_LOG_DIR}
RUN chown spring:spring ${APP_LOG_DIR}

# Copy build output to destination path (e.g. /opt/appname/application.jar)
COPY target/*.jar ${APP_HOME_DIR}/application.jar

##### Run

USER spring:spring

WORKDIR ${APP_HOME_DIR}

ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "application.jar"]