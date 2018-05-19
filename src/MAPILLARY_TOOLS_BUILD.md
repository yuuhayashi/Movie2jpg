[タイムラプス動画ファイルから静止画を生成する](README.md)
----

# mapillary_tools のインストールとビルド

## Dockerのインストール

 * ここでは [Docker]()についての説明は省略します。各自ネットで調べてください。
 * **Docker**をインストールしてください。インストール手順はネットで調べてください。


## dockerイメージの build

 * [mapillary_tools](https://github.com/mapillary/mapillary_tools)

### 'Linux編'

1. [mapillary_tools-master.zip](https://github.com/mapillary/mapillary_tools/archive/master.zip) をダウンロードして解凍する  
フォルダ `mapillary_tools-master` が作成される

2. 作成されたフォルダ `mapillary_tools-master` をホームディレクトリ直下に移動する

3. `~/mapi/mapillary_tools-master/mapillary.sh` をテキストエディタで開いて、下記のように書き換えてください  
  ```
  export MAPILLARY_EMAIL="hayashi.yuu@gmail.com"
  export MAPILLARY_PASSWORD="password"
  export MAPILLARY_USERNAME="hayashi"
  export MAPILLARY_PERMISSION_HASH="eyJleHBpcmFiMjAyMC....F1dfQ=="
  export MAPILLARY_SIGNATURE_HASH="SwRGN.....GI="
  
  python /source/mapillary_tools/python/remove_duplicates.py /mnt/mapi/img/m/ /mnt/mapi/img/duplicate/
  python /source/mapillary_tools/python/upload_with_preprocessing.py /mnt/mapi/img/m/
  ```

4. `~/mapi/mapillary_tools-master/Dockerfile` をテキストエディタで開いて、下記のように書き換えてください  
  ```
  FROM ubuntu:16.04
  
  # SETUP
  RUN \
    apt-get -qq update && \
    apt-get -yqq install \
        git \
        python-pip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
  
  RUN \
    pip install --upgrade pip
  
  RUN mkdir /mnt/mapi
  
  COPY . /source/mapillary_tools
  
  WORKDIR /source/mapillary_tools
  
  RUN pip install -r python/requirements.txt
  
  COPY ./mapillary.sh /root
  ```

5. `Terminal` に下記コマンドを打ち込む(build)
  ```
  mkdir ~/mapi
  mkdir ~/mapi/Movie
  mkdir ~/mapi/img
  cd ~/mapillary_tools-master
  docker build -t mapillary_tools .
  ```
  小一時間かかります


----

[Dockerfile](/gitbucket/yuu/Movie2jpg/blob/master/Dockerfile)

  ```
$ cd ~/workspace/Movie2jpg
$ docker build -t haya4/movie2jpg .
  ```