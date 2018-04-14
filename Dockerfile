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

COPY ./dist/Movie2jpg.jar /root
COPY ./lib/commons-imaging-1.0-20170205.201009-115.jar /root
COPY ./src/mapillary.sh /root

WORKDIR /mnt/osm

