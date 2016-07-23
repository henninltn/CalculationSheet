package com.github.henninltn.parsercombinator;

import java.lang.reflect.Array;
import java.util.List;
import java.util.ArrayList;

/**
 * 1つの引数を取って結果を生成する
 * parseを関数メソッドにもつ関数インターフェース
 * また、いくつかの補助的なメソッドを持つ
 * @param <T>
 */
@FunctionalInterface
public interface Parser<T> {

    T parse(Scanner scnr) throws Exception;

    /**
     * 自身のパースを実行し、結果を文字列にして返す
     * @param scnr
     * @return
     * @throws Exception
     */
    default String parseToString(Scanner scnr) throws Exception {
        return toString(parse(scnr));
    }

    /**
     * 文字または文字列を判定
     * @param obj
     * @return
     */
    static boolean isCharOrString(Object obj) {
        int len = Array.getLength(obj);
        for (int i = 0; i < len; ++i) {
            Object o = Array.get(obj, i);
            if (!(o instanceof Character || o instanceof String)) {
                return false;
            }
        }
        return true;
    }

    /**
     * オブジェクトに関する文字列への変換を定義
     * @param obj
     * @return
     */
    static String toString(Object obj) {
        if (obj.getClass().isArray()) {
            int len = Array.getLength(obj);
            StringBuilder sb = new StringBuilder();
            if (isCharOrString(obj)) {
                for (int i = 0; i < len; ++i) {
                    sb.append(Array.get(obj, i));
                }
            } else {
                for (int i = 0; i < len; ++i) {
                    sb.append(i == 0 ? "[" : ",");
                    sb.append(toString(Array.get(obj, i)));
                }
                sb.append("]");
            }
            return sb.toString();
        } else if (obj instanceof List) {
            return toString(((List) obj).toArray());
        }
        return obj.toString();
    }

    /**
     * 自身のパース関数の後に引数のパーサのパース関数を実行
     * @param prsr
     * @return
     */
    default Parser<T> or(Parser<T> prsr) {
        return scnr -> {
            T ret;
            Scanner bak = scnr.clone();
            try {
                ret = parse(scnr);
            } catch (Exception e) {
                if (!scnr.equals(bak)) {
                    throw e;
                }
                ret = prsr.parse(scnr);
            }
            return ret;
        };
    }

    /**
     * 例外を発生させるパーサを設定
     * @param errMsg
     * @return
     */
    default Parser<T> left(String errMsg) {
        return or(scnr -> {
            throw new Exception(scnr.ex(errMsg));
        });
    }

    /**
     * 引数のパーサについて、その結果を無視し、自身のパースの結果のみを返すように結合したパーサを返す
     * @param prsr
     * @return
     */
    default Parser<T> prev(Parser prsr) {
        return scnr -> {
            T ret = parse(scnr);
            prsr.parse(scnr);
            return ret;
        };
    }

    /**
     * 引数のパーサについて、そのパース結果のみを返し、自身のパース結果を無視するように結合したパーサを返す
     * @param prsr
     * @param <TR>
     * @return
     */
    default <TR> Parser<TR> next(Parser<TR> prsr) {
        return scnr -> {
            parse(scnr);
            return prsr.parse(scnr);
        };
    }

    /**
     * 自身のパースに失敗した時、それを無視して引数のパーサを実行するように結合したパーサを返す
     * @param prsr
     * @return
     */
    default Parser<List<T>> option(Parser<T> prsr) {
        return scnr -> {
            List<T> list = new ArrayList<>();
            try {
                list.add(parse(scnr));
            } catch (Exception e) {
                // 何もしない
            }
            list.add(prsr.parse(scnr));
            return list;
        };
    }
}
