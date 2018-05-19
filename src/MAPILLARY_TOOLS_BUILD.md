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

3. `Terminal` に下記コマンドを打ち込む(build)
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
