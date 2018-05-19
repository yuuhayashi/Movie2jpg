[タイムラプス動画ファイルから静止画を生成する](README.md)
----

# Docker 'haya4/movie2jpg'

## Dockerのインストール

 * ここでは [Docker]()についての説明は省略します。各自ネットで調べてください。
 * **Docker**をインストールしてください。インストール手順はネットで調べてください。


## dockerイメージの build

### 'Linux編'

1. [Movie2jpg-master.zip](/gitbucket/yuu/Movie2jpg/archive/master.zip) をダウンロードして解凍する  
フォルダ `Movie2jpg-master` が作成される

2. 作成されたフォルダ `Movie2jpg-master` をホームディレクトリ直下に移動する

3. `Terminal` に下記コマンドを打ち込む(build)
  ```
  cd ~/Movie2jpg-master
  mkdir mapi
  mkdir mapi/Movie
  mkdir mapi/img
  cp Movie2jpg.ini mapi/Movie
  docker build -t haya4/movie2jpg .
  ```
  小一時間かかります


### 'Windows編'

1. [Movie2jpg-master.zip](/gitbucket/yuu/Movie2jpg/archive/master.zip) をダウンロードして解凍する  
フォルダ `Movie2jpg-master` が作成される

2. 作成されたフォルダ `Movie2jpg-master` をCドライブの直下に移動する

3. `Docker Quickstart Terminal` に下記コマンドを打ち込む(build)
  ```
  cd /c/Movie2jpg-master
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
