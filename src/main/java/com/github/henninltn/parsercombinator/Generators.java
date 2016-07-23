package com.github.henninltn.parsercombinator;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.BiFunction;

/**
 * パーサジェネレータ、パーサコンビネータを静的なメソッド、または関数のフィールドとして提供
 */
public class Generators {

    /**
     * 作成したパーサのテスト用のラッパー
     * @param prsr テスト対象のパーサ
     * @param src  構文解析の対象となる文字列
     * @param <T>
     */
    public static final <T> void parseTest(Parser<T> prsr, String src) {
        Scanner scnr = new Scanner(src);
        try {
            System.out.println(prsr.parseToString(scnr));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * いずれかの1文字をパース
     */
    public static final Parser<Character> anyChar = satisfy(chr -> true);

    /**
     * 条件を満たす1文字をパース
     * @param fn
     * @return
     */
    public static final Parser<Character> satisfy(Function<Character, Boolean> fn) {
        return scnr -> {
            char chr = scnr.peek();
            if (!fn.apply(chr)) {
                throw new Exception(scnr.ex("not satisfy"));
            }
            scnr.next();
            return chr;
        };
    }

    /**
     * 任意の1文字をパース
     * @param chr
     * @return
     */
    public static final Parser<Character> char1(char chr) {
        return satisfy(c -> c == chr).left("not char '" + chr + "'");
    }

    /**
     * アルファベットまたは数字を判定
     * @param chr
     * @return
     */
    public static final boolean isAlphaNum(char chr) {
        return Character.isAlphabetic(chr) || Character.isDigit(chr);
    }

    /**
     * 数字を1文字パース
     */
    public static final Parser<Character> digit    = satisfy(Character::isDigit     ).left("not digit"   );
    /**
     * 文字を1文字パース
     */
    public static final Parser<Character> upper    = satisfy(Character::isUpperCase ).left("not upper"   );
    /**
     * 小文字を1文字パース
     */
    public static final Parser<Character> lower    = satisfy(Character::isLowerCase ).left("not lower"   );
    /**
     * アルファベットを1文字パース
     */
    public static final Parser<Character> alpha    = satisfy(Character::isAlphabetic).left("not alpha"   );
    /**
     * アルファベットまたは数字を1文字パース
     */
    public static final Parser<Character> alphaNum = satisfy(Generators  ::isAlphaNum  ).left("not alphaNum");
    /**
     * 汎用文字を1文字パース
     */
    public static final Parser<Character> letter   = satisfy(Character::isLetter    ).left("not letter"  );

    /**
     * 半角スペースまたはタブ文字を判定
     * @param chr
     * @return
     */
    public static final boolean isSpace(char chr) {
        return chr == '\t' || chr == ' ';
    }

    /**
     * 半角スペースまたはタブ文字をパース
     */
    public static final Parser<Character> space = satisfy(Generators::isSpace).left("not space");
    /**
     * 半角スペースまたはタブ文字を複数パース
     */
    public static final Parser<List<Character>> spaces = many(space);

    /**
     * 複数のパーサをand結合させる
     * @param prsrs
     * @param <T>
     * @return
     */
    public static final <T> Parser<List<T>> sequence(Parser<T>... prsrs) {
        return scnr -> {
            List<T> list = new ArrayList<>();
            for (Parser<T> prsr : prsrs) {
                list.add(prsr.parse(scnr));
            }
            return list;
        };
    }

    /**
     * 任意のパーサを任意の回数分and結合させる
     * @param n
     * @param prsr
     * @param <T>
     * @return
     */
    public static final <T> Parser<List<T>> replicate(int n, Parser<T> prsr) {
        return scnr -> {
            List<T> list = new ArrayList<>();
            for (int i = 0; i < n; ++i) {
                list.add(prsr.parse(scnr));
            }
            return list;
        };
    }

    /**
     * 任意のパーサを0回以上、失敗するまでand結合させる
     * @param prsr
     * @param <T>
     * @return
     */
    public static final <T> Parser<List<T>> many(Parser<T> prsr) {
        return scnr -> {
            List<T> list = new ArrayList<>();
            try {
                while (true) {
                    list.add(prsr.parse(scnr));
                }
            } catch (Exception e) {
            }
            return list;
        };
    }

    /**
     * 任意のパーサを1回以上、失敗するまでand結合させる
     * @param prsr
     * @param <T>
     * @return
     */
    public static final <T> Parser<List<T>> many1(Parser<T> prsr) {
        return scnr -> {
            T first = prsr.parse(scnr);
            List<T> ret = many(prsr).parse(scnr);
            ret.add(0, first);
            return ret;
        };
    }

    /**
     * 複数のパーサをor結合させる
     * @param prsrs
     * @param <T>
     * @return
     */
    public static final <T> Parser<T> or(Parser<T>... prsrs) {
        return scnr -> {
            Exception ex = new Exception("empty or");
            for (Parser<T> prsr : prsrs) {
                try {
                    return prsr.parse(scnr);
                } catch (Exception e) {
                    ex = e;
                }
            }
            throw ex;
        };
    }

    /**
     * バックトラック
     * @param prsr
     * @param <T>
     * @return
     */
    public static final <T> Parser<T> tryp(Parser<T> prsr) {
        return scnr -> {
            T ret;
            Scanner bak = scnr.clone();
            try {
                ret = prsr.parse(scnr);
            } catch (Exception e) {
                scnr.revert(bak);
                throw e;
            }
            return ret;
        };
    }

    /**
     * 任意の文字列をパース
     * @param str
     * @return
     */
    public static final Parser<String> string(String str) {
        return scnr -> {
            for (int i = 0; i < str.length(); ++i) {
                char1(str.charAt(i)).left("not string \"" + str + "\"").parse(scnr);
            }
            return str;
        };
    }

    /**
     * 例外を発生させるパーサを返す
     * @param errMsg
     * @param <T>
     * @return
     */
    public static final <T> Parser<T> left(String errMsg) {
        return scnr -> {
            throw new Exception(scnr.ex(errMsg));
        };
    }

    /**
     * パース後のデータに適用する関数を設定
     * @param fn
     * @param prsr
     * @param <T1>
     * @param <T2>
     * @return
     */
    public static final <T1, T2> Parser<T1> apply(Function<T2, T1> fn, Parser<T2> prsr) {
        return s -> fn.apply(prsr.parse(s));
    }

    /**
     * パース後のデータに適用する2引数の関数を設定
     * @param fn
     * @param prsr
     * @param <T1>
     * @param <T2>
     * @return
     */
    public static final <T1, T2> Parser<Function<T2, T1>> apply(BiFunction<T2, T2, T1> fn, Parser<T2> prsr) {
        return scnr -> {
            T2 x = prsr.parse(scnr);
            return y -> fn.apply(x, y);
        };
    }

    /**
     * パースした数字の符号を反させる関数を設定
     * @param prsr
     * @return
     */
    public static final Parser<Integer> neg(Parser<Integer> prsr) {
        return apply(x -> -x, prsr);
    }
}
