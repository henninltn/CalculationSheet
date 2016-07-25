CalculationSheet
================

GUIで入力された文字列から四則演算や変数などを解決し解析する。

## Description
テキストエリアに数式を入力し、解析結果をラベルに出力する。

構文解析器とパーサジェネレータを作成。
GUIはJavaFXを使用。

#### 現在実装済みの機能
- 整数、小数
- 四則演算
- 変数（代入文）
- ネイピア数、円周率
- 無限大
- 関数
  - 三角関数
  - 対数、指数

#### 今後追加したい機能
- 「3x」、「2log(2)」などの省略記法
- 「- sin(pi/4)」などの省略記法

## Requirement
動作環境

JDK1.8.0_92

## Usage

#### 四則演算

**input**
```
2 + 3
```
Shift + Enter

**output**
```
i[0] = 2 + 3
o[0] = 5
```

#### 代入

**input**
```
x = 2.1
```
Shift + Enter

**output**
```
i[0] = x = 2.1
o[0] = 2.1
```

#### 組み込み変数
```
e
pi
positiveInfinity
negativeInfinity
```

#### 組み込み関数
```
sin(pi)
cos(2*pi)
tan(pi/2)
log(0)
exp(e)
```

## Installation

out/artifacts/CalculationSheet_jar/main.jar

上記の場所に実行可能jarが配置されている。

## Author

[henninltn](https://github.com/henninltn)

## Reference
[Java 再帰下降構文解析 超入門](http://qiita.com/7shi/items/64261a67081d49f941e3#_reference-8ba8d52f896fdda3a7da)

