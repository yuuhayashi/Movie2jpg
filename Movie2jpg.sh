docker run -it -v $(pwd):/mnt/mapi:rw haya4/movie2jpg:2.0.0 java -cp .:/root/Movie2jpg-2.0.0-jar-with-dependencies.jar osm.surveyor.movie2jpg.Movie2jpg /mnt/mapi
