#--------------------------------------------------------------------------
# docker build -t haya4/movie2jpg .
# mkdir mapi
# docker run -it -v $(pwd):/mnt/mapi haya4/movie2jpg bash
#-----
FROM debian

# SETUP 
RUN apt-get -qq update
RUN apt-get -yqq install ffmpeg openjdk-8-jdk unzip

RUN mkdir /mnt/mapi

COPY ./dist/lib/commons-imaging-1.0-20170205.201009-115.jar /root
COPY ./dist/Movie2jpg.jar /root

VOLUME /mnt/mapi

WORKDIR /mnt/mapi
