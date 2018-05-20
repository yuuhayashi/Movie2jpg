[タイムラプス動画ファイルから静止画を生成する](README.md)
----

# 2.1 Docker 'haya4/movie2jpg'

## 2.1.1 Dockerのインストール

 * ここでは [Docker]()についての説明は省略します。各自ネットで調べてください。
 * **Docker**をインストールしてください。インストール手順はネットで調べてください。


## 2.1.2 dockerイメージの build

1. [Movie2jpg-master.zip](/gitbucket/yuu/Movie2jpg/archive/master.zip) をダウンロードして解凍する  
フォルダ `Movie2jpg-master` が作成される

2. 作成されたフォルダ `Movie2jpg-master` をホームディレクトリ直下に移動する

3. `Terminal` (Windowsの場合は`Docker Quickstart Terminal`)に下記コマンドを打ち込む(build)
  ```
  cd ~
  cd $(pwd)/Movie2jpg-master
  mkdir mapi
  mkdir mapi/Movie
  mkdir mapi/img
  cp Movie2jpg.ini mapi/Movie
  docker build -t haya4/movie2jpg .
  ```
  小一時間かかります


----

[Dockerfile](/gitbucket/yuu/Movie2jpg/blob/master/Dockerfile)

----
[タイムラプス動画ファイルから静止画を生成する](README.md)
