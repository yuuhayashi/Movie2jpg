# タイムラプス動画ファイルから静止画を生成する

## （[撮影編](ACTIONCAM.md)）格安アクションカムで1.0秒間隔で撮影する方法

  撮影編はこちらを参照 → [中華アクションカムについて](ACTIONCAM.md)

## タイムラプスモード設定

 * 間隔は１秒  
    GPSロガーのロギング間隔を１秒間に設定するのでそれに合わせてタイムラプスも１秒間隔で十分。  
    最小0.5秒に設定することができるが、切り出したJPEGに撮影時刻を割り当てる際に１秒単位でしか設定することができないため、1秒間隔で十分。  


動画からMapillaryへアップロードするには、下記のステップ毎の処理が必要

 1. 動画ファイルから一定間隔の静止画(JPEG)ファイルを生成する

 1. 生成された静止画ファイルの更新日時を*撮影時刻*に書き換える

 1. JPEGファイルの*撮影時刻*と*GPSロガー*の時刻情報を突き合わせて JPEGのExif情報に*位置情報*を書き込む

 1. *Mapillary-tools*を使って Mapillary サイトにJPEGファイルを一括アップロードする。

これらの処理ごとに Java,Pyson,FFMPEG などのソフトウェアのインストールと設定が必要になります。
インストールと設定は、稼働させるOS毎に異なるため、説明が煩雑になるし、それぞれのOS毎の動作検証をすることもできません。
そこで、ここでは *Docker* イメージ'haya4/movie2jpg'を使って説明をします。


## 動画ファイルから一定間隔の静止画(JPEG)ファイルを生成する

### Dockerのインストールとビルド

  **Docker 'haya4/movie2jpg'**

初回に限りDockerのインストールとビルドが必要です。

こちら → [DockerBuild](DOCKERBUILD.md) を参考にしてインストールとビルドを行ってください。


### 動画ファイルのセット

撮影した動画ファイルを所定の場所に配置します。

```
~/
┃
┗━ ~/mapi
　　　┃
　　　┣━ ~/mapi/img
　　　┃
　　　┗━ ~/mapi/Movie
　　　　　　┃
　　　　　　┣━ Movie2jpg.ini
　　　　　　┣━ XXXX.mp4
　　　　　　┗━ YYYY.mp4
```
  'Windows'の場合は「`~`」を「`/c`」に読み替えてください

 * フォルダ `~/mapi/Movie` に MP4ファイルを配置する。

 * フォルダ `~/mapi/Movie` に `Movie2jpg.ini` を配置する。

[Movie2jpg.ini](/gitbucket/yuu/Movie2jpg/blob/master/Movie2jpg.ini) の設定例  
```
[FFMPEG]
FFMPEG_OUTPUT_FRAME_RATE=30
```
  - 1.0秒間隔で撮影した場合はFFMPEG_OUTPUT_FRAME_RATE=30 (fps=30) とすると1.0間隔の静止画が取り出せる


### 静止画の切り出し処理を実行 - Docker run

実行:   

#### Linuxの場合
  ```
  cd
  docker run -it -v $(pwd)/mapi:/mnt/mapi:rw haya4/movie2jpg java -cp .:/root/Movie2jpg.jar movie2jpg.Movie2jpg ./Movie/Movie2jpg.ini

  ```

#### Windowsの場合
  ```
  docker run -it -v c:/mapi:/mnt/mapi:rw haya4/movie2jpg java -cp .:/root/Movie2jpg.jar movie2jpg.Movie2jpg /mnt/mapi/Movie/Movie2jpg.ini
  ```

  実行すると、「Movie2jpg」が起動され、`~/mapi/Movie`フォルダ内の「mp4」ファイルごとに

  `ffmpeg -ss 0 -i $(mp4 file) -f image2 -vf fps=$(FFMPEG_OUTPUT_FRAME_RATE) $(output file)`

  が実行されます。

  完了すると、`~/mapi/img`フォルダの下にMP4ファイル名と同じ名前のフォルダが作成され、その中に切り出されたJPEG画像が生成されます。

```
~/
┃
┗━ ~/mapi
　　　┃
　　　┣━ ~/mapi/img
　　　┃　　┣━ XXXX
　　　┃　　┃　　┣━ 00001.jpg
　　　┃　　┃　　┣━ 00002.jpg
　　　┃　　┃　　┣━     :
　　　┃　　┃　　┗━ 01861.jpg
　　　┃　　┃
　　　┃　　┗━ YYYY
　　　┃　　　　　┣━ 00001.jpg
　　　┃　　　　　┣━ 00002.jpg
　　　┃　　　　　┣━     :
　　　┃　　　　　┗━ 02408.jpg
　　　┃
　　　┗━ ~/mapi/Movie
　　　　　　┃
　　　　　　┣━ XXXX.mp4
　　　　　　┗━ YYYY.mp4
```
  'Windows'の場合は「`~`」を「`/c`」に読み替えてください


-----

# 連番JPEGファイルに撮影時刻を割り付ける

  もし、カメラの時刻合わせに失敗していたり、時間ウォーターマークを設定し忘れた場合は、[撮影時刻推定方法](UPDATETIME.md) を参照してください。

時間ウォーターマークが設定されている場合は、下記の方法で抽出されたイメージファイルに撮影時刻を設定することができます。


### 時間ウォーターマークを使ったファイル更新日時の再設定

4. ~img~フォルダの画像を画像ビューワなどで開き、サムネイルなどで画像が撮影された位置が特定できるファイルを見つける

  ![TimeWaterMark01](TimeWaterMark01.png)


```
$ docker run -it -v $(pwd)/mapi:/mnt/mapi haya4/movie2jpg java -cp .:/root/Movie2jpg.jar:/root/commons-imaging-1.0-20170205.201009-115.jar osm.jp.gpx.Restamp ./img/20180407_135053A 00239.jpg 2018-04-07T05:54:47Z 01725.jpg 2018-04-07T05:19:36Z
```

----

# GPSログとJPEGの更新日付を付きあわせてJPEGのEXiFに位置情報を書き込む

### AdjustTime2を使う

----

# Mapillary-tools を使って*Mapillary*に大量アップ

```
/home/yuu/Desktop
┃
┗━ ./OSM
　　　┃
　　　┗━ ./OSM/img
　　　　　　┣━ ./OSM/img/m
　　　　　　┃　　┣━ 00001.jpg
　　　　　　┃　　┣━ 00002.jpg
　　　　　　┃　　┣━     :
　　　　　　┃　　┗━ 01861.jpg
　　　　　　┃
　　　　　　┗━ ./OSM/img/duplicate
```
 * ~/home/yuu/Desktop/OSM/img/m~フォルダにMapillaryにアップしたいJPEGファイルを置く

 * ~/home/yuu/Desktop/OSM/img/duplicate~フォルダに重複ファイルが置かれる


## Docker mapillary_tools

### Dockerfile

```
FROM ubuntu:16.04

# SETUP
RUN apt-get -qq update
RUN apt-get -y upgrade
RUN \
    apt-get -qq update && \
    apt-get -yqq install \
        git \
        python-pip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

RUN \
    pip install --upgrade pip

RUN mkdir /mnt/osm

COPY . /source/mapillary_tools

WORKDIR /source/mapillary_tools

RUN pip install -r python/requirements.txt

RUN apt-get -qq update
RUN apt-get -yqq install openjdk-8-jre
```

### Docker build

~mapillary.sh~  
```
export MAPILLARY_EMAIL="hayashi.yuu@gmail.com"
export MAPILLARY_PASSWORD="yuu8844"
export MAPILLARY_USERNAME="hayashi"
export MAPILLARY_PERMISSION_HASH="eyJleHBpcmF0aW9uIjoiMjAyMC0wMS0wMVQwMDowMDowMFoiLCJjb25kaXRpb25zIjpbeyJidWNrZXQiOiJtYXBpbGxhcnkudXBsb2Fkcy5tYW51YWwuaW1hZ2VzIn0sWyJzdGFydHMtd2l0aCIsIiRrZXkiLCJoYXlhc2hpLyJdLHsiYWNsIjoicHJpdmF0ZSJ9LFsic3RhcnRzLXdpdGgiLCIkQ29udGVudC1UeXBlIiwiIl0sWyJjb250ZW50LWxlbmd0aC1yYW5nZSIsMCw1MDAwMDAwMF1dfQ=="
export MAPILLARY_SIGNATURE_HASH="SwRGN9K/FN+FpXPSO09LvuhBAGI="

# python /source/mapillary_tools/python/geotag_from_gpx.py /mnt/osm/img/m/ /mnt/osm/img/2016-04-09_150103.gpx

python /source/mapillary_tools/python/remove_duplicates.py /mnt/osm/img/m/ /mnt/osm/img/duplicate/

python /source/mapillary_tools/python/upload_with_preprocessing.py /mnt/osm/img/m/
```

```
cd /home/yuu/workspace/mapillary_tools
docker build -t haya4/movie2jpg .
```

### Docker run

```
cd /home/yuu/Desktop/workspace/Movie2jpg
docker run -it -v /home/yuu/Desktop/OSM:/mnt/osm haya4/movie2jpg /bin/bash /root/mapillary.sh

-v /home/yuu/Desktop/OSM:/mnt/osm
    PCのフォルダ(/home/yuu/Desktop/OSM)をコンテナのフォルダ(/mnt/osm)にマウントする

# cd /root
# sh ./mapillary.sh
#     :
#
```
