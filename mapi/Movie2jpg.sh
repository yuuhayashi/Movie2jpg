cd ~/docker/Movie2jpg-master
docker run -it -v $(pwd)/mapi:/mnt/mapi:rw movie2jpg java -cp .:/root/Movie2jpg.jar:/root/commons-imaging-1.0-20170205.201009-115.jar movie2jpg.Movie2jpg ./Movie/Movie2jpg.ini

