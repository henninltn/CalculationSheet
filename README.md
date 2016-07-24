CalculationSheet
================

GUIで入力された文字列から四則演算や変数などを解決し解析する。

## Description
テキストエリアに数式を入力し、解析結果をラベルに出力する。

構文解析器とパーサジェネレータを作成。
GUIはJavaFXを使用。

現在実装済みの機能は以下。
- 整数、小数に対応
- 四則演算
- 変数（代入文）

参考 [Java 再帰下降構文解析 超入門](http://qiita.com/7shi/items/64261a67081d49f941e3#_reference-8ba8d52f896fdda3a7da)

## Requirement
動作環境

JDK1.8.0_92

## Usage

- 四則演算

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

- 代入

**input**
```
x = 2
```
Shift + Enter

**output**
```
i[0] = x = 2
o[0] = 2
```

## Install

out/artifacts/CalculationSheet_jar/main.jar

上記の場所に実行可能jarが配置されている

## Author

[henninltn](https://github.com/henninltn)
