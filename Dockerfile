FROM maven:3.8.5-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests -Dtests.skip=true -Dskip.unit.tests=true

FROM eclipse-temurin:17-jre-alpine
LABEL maintainer="Paulo Gomes da Cruz Junior <paulushc@gmail.com>"

WORKDIR /usr/share/service/
RUN mkdir -p /usr/share/service/config &&\
    mkdir -p /usr/share/service/dump &&\
    mkdir -p /usr/share/service/public

ENV LANG en_CA.UTF-8
ENV LANGUAGE en_CA.UTF-8
ENV LC_ALL en_CA.UTF-8

ENV HEAP_LOG_PATH /usr/share/service/dump
ENV JAVA_OPS -Xms256m -Xmx512m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$HEAP_LOG_PATH
ENV JAVA_DEBUG_OPS -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:$HEAP_LOG_PATH/garbage-collection.log
ENV DEBUG_MODE false

COPY --from=build /home/app/target/*.jar /usr/share/service/service.jar
COPY ./dockerfile-entrypoint.sh /usr/share/service/dockerfile-entrypoint.sh
RUN chmod +x /usr/share/service/dockerfile-entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/usr/share/service/dockerfile-entrypoint.sh"]

