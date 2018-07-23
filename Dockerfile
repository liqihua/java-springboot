FROM centos:latest
MAINTAINER liqihua
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo 'Asia/Shanghai' >/etc/timezone
RUN yum -y install java-1.8.0-openjdk*
ADD ./target/java.war /opt/aa/bb/java.war
WORKDIR /opt/aa/bb/
ENTRYPOINT java -jar -Dserver.port=8080 java.war
