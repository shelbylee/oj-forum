FROM maven:3-jdk-11
ADD . /opt/oj/oj-forum
WORKDIR /opt/oj/oj-forum
EXPOSE 8081
RUN mvn package -Dmaven.test.skip=true