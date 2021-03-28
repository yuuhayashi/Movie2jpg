cd ~/docker/Movie2jpg-master
docker run -it -v $(pwd):/mnt/mapi:rw movie2jpg java -cp .:/root/Movie2jpg.jar osm.surveyor.movie2jpg.Movie2jpg mapi
