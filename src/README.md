# タイムラプス動画ファイルから静止画を生成する

中華アクションカムで撮影したタイムラプス動画ファイルから１秒毎の静止画を切り出す。


### 中華アクションカムの問題点

 * タイムラプス撮影を行うと0.5秒ピッチから設定できるが、生成されるのはH265形式の動画ファイルのみとなる。

 * インターバル撮影モードにするとJPEGファイルが一定間隔で生成されるが、設定できる時間が「最小３秒」と間隔が長すぎる。

 * インターバル撮影モードは撮影時のビープ音がうるさい。

Mapillaryにアップするなら１秒間隔にしたいので **タイムラプス撮影** に挑戦してみる


## タイムラプスモード設定

 * 間隔は0.5秒  
    GPSロガーのロギング間隔を１秒間に設定するのでそれに合わせてタイムラプスも１秒間隔で十分に思えますが、切り出したJPEGに撮影時刻を割り当てる際に、GPSロガーの間隔とJPEG時刻の間隔にズレが生じます。そのズレを補正するために１秒より短い間隔で１／２秒に設定する。

 * 出力形式はMP4(h265)になってしまう  
    出力されるのはカメラの仕様上MP4(h265)になります。可能ならばMOV(mjpeg)形式ならベター

 * リアルタイム動画は？  
    タイムラプスではなくそのまま動画で撮影した場合は？ もちろんタイムラプスに比べて動画のファイルサイズは大きくなります。しかし、SDカードの容量がいっぱいになる前にバッテリーのほうが先になくなるのでファイルサイズはあまり問題になりません。  
リアルタイム動画ではコマ落ちの可能性が高くなるような気がします（個人の感想です）。JPEGに撮影時刻を割り当てる際に時間のズレが生じる可能性が高くなります。


## Docker 'haya4/Movie2jpg'

### docker build

`Dockerfile`
```
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


```

```
$ cd /home/yuu/workspace/Movie2jpg
$ docker build -t haya4/movie2jpg .
```

### 動画ファイルのセット

撮影した動画ファイルを所定の場所に配置します。

```
/home/yuu/Desktop
┃
┗━ ./OSM
　　　┃
　　　┣━ ./OSM/img
　　　┃
　　　┗━ ./OSM/Movie
　　　　　　┃
　　　　　　┣━ XXXX.mp4
　　　　　　┗━ YYYY.mp4

```

 * フォルダ `/home/yuu/Desktop/OSM/Movie` に MP4ファイルを配置する。



### Docker run

実行
```
$ docker run -it -v /home/yuu/Desktop/OSM:/mnt/osm haya4/movie2jpg ffmpeg -ss 0  -i Movie/20180401_071732A.mp4 -f image2 -r 15 img/%05d.jpg

$ docker run -it -v /home/yuu/Desktop/OSM:/mnt/osm haya4/movie2jpg java -cp .:/root/Movie2jpg.jar movie2jpg.Movie2jpg

```

完了すると、`./OSM/img`フォルダの下にMP4ファイル名と同じ名前のフォルダが作成され、その中に切り出されたJPEG画像が生成されます。

```
/home/yuu/Desktop
┃
┗━ ./OSM
　　　┃
　　　┣━ ./OSM/img
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
　　　┗━ ./OSM/Movie
　　　　　　┃
　　　　　　┣━ XXXX.mp4
　　　　　　┗━ YYYY.mp4
```

-----

# 連番JPEGファイルに撮影時刻を割り付ける

### 用意するもの

 * 連番JPEGファイル 一定間隔で撮影されたもの（動画から切り出したもの）

 * GPSロガー（GPX)ファイル

1. [JOSM](https://josm.openstreetmap.de/)を起動する

2. 起動したJOSMにGPXファイルをドラッグ＆ドロップする(複数のGPXがあるときは全部まとめてドロップする)

3. 


