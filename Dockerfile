#--------------------------------------------------------------------------
# docker build -t haya4/movie2jpg .
# docker run -p 80:8080 -it -v /home/yuu/Desktop/OSM:/mnt/osm haya4/movie2jpg bash
#-----
FROM haya4/mapillary-tools

# SETUP 
RUN apt-get -qq update && \
    apt-get -y upgrade
RUN \
    apt-get -qq update && \
    apt-get -yqq install ffmpeg
RUN \
    apt-get -qq update && \
    apt-get -yqq install openjdk-8-jdk
RUN apt-get -yqq install unzip

COPY ./java_ee_sdk-8.zip /root
RUN \
    cd /root && \
    unzip java_ee_sdk-8.zip
COPY ./lib/commons-imaging-1.0-20170205.201009-115.jar /root

COPY ./dist/Movie2jpg.jar /root

WORKDIR /mnt/osm
