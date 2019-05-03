FROM maven:3-jdk-11
ADD . /opt/oj/oj-forum
WORKDIR /opt/oj/oj-forum
RUN mvn spring-boot:run
EXPOSE 8081