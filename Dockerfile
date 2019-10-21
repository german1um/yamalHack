FROM gradle:5.4.1-jdk8 as stage

COPY . /build/
WORKDIR /build
RUN gradle bootJar

FROM java:8

COPY --from=stage /build/build/libs/build-0.0.1-SNAPSHOT.jar /app/init.jar
WORKDIR /app
CMD ["java", "-jar", "init.jar"]