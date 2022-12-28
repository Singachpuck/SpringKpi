FROM maven:3.8.6-amazoncorretto-17

ENV DB_URL=jdbc:postgresql://dpg-celogjcgqg4ekma2thd0-a.frankfurt-postgres.render.com:5432/tender_system
ENV DB_USERNAME=user

COPY . /opt

WORKDIR /opt

RUN mvn clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/TenderSystem-6.0.0-SNAPSHOT.jar", "--spring.config.location=src/main/resources/application.properties"]
