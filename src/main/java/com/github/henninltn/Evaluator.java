package com.github.henninltn;

import static com.github.henninltn.parsercombinator.Generators.*;
import com.github.henninltn.parsercombinator.Parser;
import com.github.henninltn.parsercombinator.Scanner;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 四則演算や変数解決などを行う構文解析器
 * フィールドの関数に関して、副作用のないもの、または副作用のある関数、メソッドを呼び出さないものは静的に定義してある
 */
public class Evaluator {

    // 外部で生成した変数の用のスコープを参照するための変数
    private Map<String, Integer> globalScope;

    /**
     * グローバススコープへの参照を受け取る
     * @param globalScope
     */
    public Evaluator(Map<String, Integer> globalScope) {
        this.globalScope = globalScope;
    }

    private static final Function<List<Character>, Integer> toInt = chrList -> Integer.parseInt(Parser.toString(chrList));
    private static final Function<Object, String> toString = obj -> Parser.toString(obj);

    private static final Parser<String> variableName  = tryp(scnr -> {
        String ret = String.valueOf(alpha.parse(scnr));
        try {
            ret += Parser.toString(many1(alphaNum).parse(scnr));
        } catch (Exception e) {
            // 何もしない
        }
        return ret;
    });

    private static final Parser<Integer> number = apply(toInt, many1(digit));

    private final Parser<Integer> variable = tryp(scnr -> {
        String varName = variableName.parse(scnr);
        if(!globalScope.containsKey(varName)) left("variable not existed").parse(scnr);
        int ret = globalScope.get(varName);
        return ret;
    });

    private static final <T> T accumulate(T x, List<Function<T, T>> fns) {
        for (Function<T, T> fn : fns) {
            x = fn.apply(x);
        }
        return x;
    }

    // term, exprの共通部分を抜き出したもの
    private static final <T> Parser<T> eval(Parser<T> m, Parser<List<Function<T, T>>> fns) {
        return scnr -> {
            T x = m.parse(scnr);
            return accumulate(x, fns.parse(scnr));
        };
    }

    // 前方参照のためのラッパー
    // expr, term, factorが再帰的に定義されているため
    // ラムダ式は前方参照できないため無名クラス
    private final Parser<Integer> factor = new Parser<Integer>() {
        @Override
        public Integer parse(Scanner scnr) throws Exception {
            return factor_.parse(scnr);
        }
    };

    // EBNF: term = factor, {("*", factor) | ("/", factor)}
    @SuppressWarnings("unchecked")
    private final Parser<Integer> term = Evaluator.<Integer>eval(factor, many(or(
            char1('*').next(apply((y, z) -> z * y, factor)),
            char1('/').next(apply((y, z) -> z / y, factor))
    )));

    // EBNF: expr = term, {("+", term) | ("-", term)}
    @SuppressWarnings("unchecked")
    private final Parser<Integer> expr = Evaluator.<Integer>eval(term, many(or(
            char1('+').next(apply((y, z) -> z + y, term)),
            char1('-').next(apply((y, z) -> z - y, term))
    )));

    // EBNF: factor = [spaces], ("(", expr, ")") | number | variable
    @SuppressWarnings("unchecked")
    private final Parser<Integer> factor_ = spaces.next(
            or(char1('(').next(expr).prev(char1(')')), number, variable)
    ).prev(spaces);

    // EBNF: assign = [spaces], variableName, [spaces], "=", expr
    @SuppressWarnings("unchecked")
    private final Parser<Integer> assign = tryp((scnr) -> {
        String varName = spaces.next(variableName).prev(spaces).parse(scnr);
        int value = char1('=').next(expr).parse(scnr);
        globalScope.put(varName, value);
        return value;
    });

    /**
     * 渡された文字列を構文解析する
     * 例外が発生した場合、エラーメッセージを返却
     * @param src 構文解析の対象文字列
     * @return
     */
    public final String eval(String src) {
        Scanner scnr = new Scanner(src);
        try {
            Parser ret;
            if (src.matches(".*=.*")) {
                ret = assign;
            } else {
                ret = expr;
            }
            return ret.parseToString(scnr);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
