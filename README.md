# 時間管理をするアプリ（ChocoAlate）

[![IMAGE ALT TEXT HERE](https://jphacks.com/wp-content/uploads/2023/07/JPHACKS2023_ogp.png)](https://www.youtube.com/watch?v=yYRQEdfGjEg)

## 製品概要
### 背景(製品開発のきっかけ、課題等）
待ち合わせの集合時間に間に合うために何時に起きたらよいかわからず、予定よりかなり早く到着してしまったり逆に見通しが甘く、5分10分遅刻したりすることがあります。

そのため、予定時間に遅れずなおかつ早すぎない時間につくルートを調べる必要があります。

しかし、そのルート検索は面倒であり、なおかつ準備時間などが考慮されていません。

### 製品説明（具体的な製品の説明）
到着の場所と時間に合わせて起きる時間,出発時間や移動手段を調べてくれます。


### 特長
#### 1. 特長1
アプリを起動して目的地と到着時間を入力するだけですぐに出発時間を検索可能。

#### 2. 特長2
お気に入り登録することによって、よく使う条件をカンタン入力可能。

#### 3. 特長3
歩く速度や準備時間の設定が可能で、個人個人に合わせたスタイルの検索が可能。

### 解決出来ること
違うアプリをまたいで道順や公共交通機関の時間などを調べること

### 今後の展望
- 各々の歩く速度に合わせて到着時刻を正確に予測することができるようにする機能
- 

### 注力したこと（こだわり等）
* アプリのデザインを限られた時間内で工夫した
* メイン機能だけではなく、検索候補が複数ある時の選択や、歩く速度、準備時間の設定といったユーザーに寄り添う機能を実装した
* ロゴはすべて1から制作

## 開発技術
### 活用した技術
#### API・データ
* Navitime Rapid API
* Google Maps API(Maps SDK, Directions) 

#### フレームワーク・ライブラリ・モジュール
* Libraries for Jetpack Compose
* Material Dialog(io.github.vanpra.compose-material-dialogs:core, io.github.vanpra.compose-material-dialogs:datetime)
* Google Map Services(com.google.maps:google-maps-services)
* slf4j(org.slf4j:slf4j-simple)
* Maps Compose(com.google.maps.android:maps-compose, com.google.android.gms:play-services-maps)
* Datastore(androidx.datastore:datastore-preferences)
* Room(androidx.room:room-runtime, "androidx.room:room-compiler)
* OkHttp(com.squareup.okhttp3:okhttp)
* kotlin-result(com.michael-bull.kotlin-result:kotlin-result)
* Material Icons Extended(androidx.compose.material:material-icons-extended)

#### デバイス
* Android


### 独自技術
#### ハッカソンで開発した独自機能・技術
* https://github.com/jphacks/OL_2331/blob/ce24cc8c6767fde47738699658da01b23ae63c7d/app/src/main/java/jp/nitech/edamame/steps/StepsApplication.kt
* 

#### 製品に取り入れた研究内容（データ・ソフトウェアなど）（※アカデミック部門の場合のみ提出必須）
* 
* 
