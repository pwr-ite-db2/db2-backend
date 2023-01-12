FROM marcinlenki/bazy-danych-server-side-app:latest
#EXPOSE 443
EXPOSE 8080
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]