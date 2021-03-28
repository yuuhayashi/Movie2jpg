#--------------------------------------------------------------------------
# docker build -t haya4/movie2jpg .
# mkdir mapi
# docker run -it -v $(pwd):/mnt/mapi haya4/movie2jpg bash
#-----
FROM ubuntu:18.04

# SETUP 
RUN apt-get -qq update
RUN apt-get -yqq install ffmpeg openjdk-8-jdk unzip

RUN mkdir /mnt/mapi

#COPY ./dist/Movie2jpg.jar /root

VOLUME /mnt/mapi

WORKDIR /mnt/mapi
