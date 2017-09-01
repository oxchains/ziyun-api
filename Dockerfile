FROM openjdk:8-jdk-alpine
ADD target/ziyun-demo-1.0.jar app.jar
ENTRYPOINT [ "sh", "-c", "java -jar /app.jar" ]
