FROM gradle:5.4.1-jdk8 as stage

COPY . /build/
WORKDIR /build
RUN gradle bootJar

FROM sergush/java-opencv-py

COPY --from=stage /build/build/libs/build-0.0.1-SNAPSHOT.jar /app/init.jar
WORKDIR /app

EXPOSE  50001

CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,address=50001,suspend=n", "-jar", "init.jar"]