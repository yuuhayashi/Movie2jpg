#--------------------------------------------------------------------------
# docker build -t haya4/movie2jpg .
# mkdir mapi
# docker run -it -v $(pwd)/mapi:/mnt/mapi haya4/movie2jpg bash
#-----
FROM ubuntu:16.04

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
RUN mkdir /mnt/mapi

COPY ./dist/lib/commons-imaging-1.0-20170205.201009-115.jar /root

COPY ./dist/Movie2jpg.jar /root

MOUNT /mnt/mapi

WORKDIR /mnt/mapi
