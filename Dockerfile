FROM ubuntu:16.04

# SETUP
RUN apt-get -qq update && \
    apt-get -y upgrade
RUN \
    apt-get -qq update && \
    apt-get -yqq install ffmpeg
RUN \
    apt-get -qq update && \
    apt-get -yqq install openjdk-8-jre

RUN mkdir /mnt/osm

COPY ./dist/Movie2jpg.jar /root

WORKDIR /mnt/osm

