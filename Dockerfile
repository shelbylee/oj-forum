FROM maven:3-jdk-11
ADD . /opt/oj/oj-forum
WORKDIR /opt/oj/oj-forum
RUN mvn package spring-boot:repackage
EXPOSE 8081