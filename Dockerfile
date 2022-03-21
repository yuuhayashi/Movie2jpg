#--------------------------------------------------------------------------
# docker build -t haya4/movie2jpg:2.0.1 .
# docker login
# 	Username: haya4
# 	Password: xxxxxx
#   Login Succeeded
# docker tag c3e80b5d2119 haya4/movie2jpg:2.0.1
# docker push haya4/movie2jpg:2.0.1
# 
# mkdir target
# docker run --rm -v $(pwd):/mnt/mapi haya4/movie2jpg:2.0.1
#
#-----
FROM alpine:3.15.1
RUN apk add ffmpeg \
 && apk --no-cache add openjdk11 \
 && rm -rf /var/cache/apk/*
ADD https://github.com/yuuhayashi/Movie2jpg/releases/download/v2.0.1/Movie2jpg-jar-with-dependencies.jar /movie2jpg-jar-with-dependencies.jar
RUN mkdir /mnt/mapi
VOLUME /mnt/mapi
WORKDIR /mnt/mapi
ENTRYPOINT ["java", "-jar", "/movie2jpg-jar-with-dependencies.jar", "/mnt/mapi"]
