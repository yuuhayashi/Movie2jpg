#--------------------------------------------------------------------------
# docker build -t haya4/movie2jpg:2.0.0 .
# docker login
# 	Username: haya4
# 	Password: xxxxxx
#   Login Succeeded
# docker tag c3e80b5d2119 haya4/movie2jpg:2.0.0
# docker push haya4/movie2jpg:2.0.0
# 
# mkdir target
# docker run -it -v $(pwd):/mnt/mapi haya4/movie2jpg:2.0.0 bash
# 	java -cp .:/root/Movie2jpg-2.0.0-jar-with-dependencies.jar osm.surveyor.movie2jpg.Movie2jpg /mnt/mapi
#
# docker run -it -v $(pwd):/mnt/mapi:rw haya4/movie2jpg:2.0.0 java -cp .:/root/Movie2jpg-2.0.0-jar-with-dependencies.jar osm.surveyor.movie2jpg.Movie2jpg /mnt/mapi
#-----
FROM ubuntu:18.04

# SETUP 
RUN apt-get -qq update
RUN apt-get -yqq install ffmpeg openjdk-8-jdk unzip

RUN mkdir /mnt/mapi

COPY ./target/Movie2jpg-2.0.0-jar-with-dependencies.jar /root

VOLUME /mnt/mapi

WORKDIR /mnt/mapi
