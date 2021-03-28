# 2.1 Docker 'haya4/movie2jpg'

## 2.1.1 Dockerのインストール

 * ここでは [Docker]()についての説明は省略します。各自ネットで調べてください。
 
 * **Docker**をインストールしてください。インストール手順はネットで調べてください。


## 2.1.2 dockerイメージの build

1. [GitHub](https://github.com/yuuhayashi/Movie2jpg) からソースを取得

  ```
  $ cd workspace
  $ git clone https://github.com/yuuhayashi/Movie2jpg.git
  ```

2. docker build

  ```
  $ docker build -t haya4/movie2jpg:2.0.0 .
  ```


3. docker-hub へプッシュする

(1) docker-hub に sign-in する

  ```
  $ docker login
  	  Username: haya4
  	  Password: xxxxxx
      Login Succeeded
  ```

(2) docker-hub へプッシュ

  ```
  docker tag c3e80b5d2119 haya4/movie2jpg:2.0.0
  docker push haya4/movie2jpg:2.0.0
  ```
  
  - `c3e80b5d2119`はdockerコンテナイメージのID
  

(3) dockerコンテナ内での実行

  ```
  $ docker run -it -v $(pwd):/mnt/mapi haya4/movie2jpg:2.0.0 bash
  
    # 	java -cp .:/root/Movie2jpg-2.0.0-jar-with-dependencies.jar osm.surveyor.movie2jpg.Movie2jpg /mnt/mapi

# docker run -it -v $(pwd):/mnt/mapi:rw haya4/movie2jpg:2.0.0 java -cp .:/root/Movie2jpg-2.0.0-jar-with-dependencies.jar osm.surveyor.movie2jpg.Movie2jpg /mnt/mapi
  ```

----

4. 実行

  ```
  $ docker run -it -v $(pwd):/mnt/mapi:rw haya4/movie2jpg:2.0.0 java -cp .:/root/Movie2jpg-2.0.0-jar-with-dependencies.jar osm.surveyor.movie2jpg.Movie2jpg /mnt/mapi
  ```

 - [Movie2jpg.sh](https://github.com/yuuhayashi/Movie2jpg/blob/master/Movie2jpg.sh)

----

- [Dockerfile](https://github.com/yuuhayashi/Movie2jpg/blob/master/Dockerfile)

