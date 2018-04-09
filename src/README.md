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
  ![DragAndDrop](GpxDrop.png)

3. 背景画像を選択する（お好みで）  
  ![MenuImage](MenuImageBing.png)

4. ~img~フォルダの画像を画像ビューワなどで開き、サムネイルなどで画像が撮影された位置が特定できるファイルを見つける
 * トンネルの入り際
 * 橋の開始／終点
 * 交差点に侵入
  などが位置を特定しやすい。
  ![ImagePosition](ImagePosition.png)

  この例では、交差点の侵入（横断歩道のところ）

5. GPXの位置を特定します。GPXのままではわかりづらいのでGPXのところを *右クリック* して「データレイヤーへ変換」を選択  
  ![GPX2Data](GPX2Data.png)

6. GPXのノードが選択できるようになったので、イメージファイルの位置のノードを*クリック*して、メニュー「*表示*」-「*詳細な情報*」を選択する  
  ![ShowInfo](ShowInfo.png)

7. 表示された「詳細なオブジェクト情報」の「*編集日時*」を記録する  
  ![ShowInfoData](ShowInfoData.png)

8. 同様にしてもうひとつの位置がわかる画像を探して、そのGPX位置の「*編集日時*」を記録する

```
ノード: -53809
  データセット: 35129fd7
  編集日時: 2018-04-07T05:19:36Z
  編集者: <新しいオブジェクト>
  バージョン: 0
  変更セット: 0
  座標: 35.3778580483, 139.2325930484
  座標(投影後): 1.549930135997497E7, 4215349.844150893
  UTMゾーン: 54N
  所属: 
    ウェイ: -54607

イメージファイル: 01725.jpg
```

9. JPEGファイルの更新日付を書き換える

```
$ docker run -it -v /home/yuu/Desktop/OSM:/mnt/osm haya4/movie2jpg java -cp .:/root/Movie2jpg.jar:/root/commons-imaging-1.0-20170205.201009-115.jar osm.jp.gpx.Restamp ./img/20180407_135053A 00239.jpg 2018-04-07_13:54:47 01725.jpg 2018-04-07_14:19:36
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
docker build -t haya4/mapillary .
```

### Docker run

```
cd /home/yuu/Desktop/workspace/mapillary-tools
docker run -it -v /home/yuu/Desktop/OSM:/mnt/osm haya4/mapillary /bin/bash

-v /home/yuu/Desktop/OSM:/mnt/osm
    PCのフォルダ(/home/yuu/Desktop/OSM)をコンテナのフォルダ(/mnt/osm)にマウントする

# cd /root
# sh ./mapillary.sh
#     :
#
```







