FROM openjdk:11
LABEL maintainer="zaragatchinay@yandex.ru"
VOLUME /tmp
EXPOSE 80
ARG JAR_FILE=build/libs/demo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]