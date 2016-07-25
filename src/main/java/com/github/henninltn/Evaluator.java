package com.github.henninltn;

import static com.github.henninltn.parsercombinator.Generators.*;
import com.github.henninltn.parsercombinator.Parser;
import com.github.henninltn.parsercombinator.Scanner;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

/**
 * 四則演算や変数解決などを行う構文解析器
 * フィールドの関数に関して、副作用のないもの、または副作用のある関数、メソッドを呼び出さないものは静的に定義してある
 */
public class Evaluator {

    // 外部で生成した変数の用のスコープを参照するための変数
    private GlobalScope globalScope;

    /**
     * グローバススコープへの参照を受け取る
     * @param globalScope
     */
    public Evaluator(GlobalScope globalScope) {
        this.globalScope = globalScope;
    }

    private static final Function<List<Character>, BigDecimal> toBigDecimal = chrList -> BigDecimal.valueOf(Double.parseDouble(Parser.toString(chrList)));
    private static final Function<Object, String> toString = obj -> Parser.toString(obj);

    private static final Parser<String> identifier  = tryp(scnr -> {
        String ret = String.valueOf(alpha.parse(scnr));
        try {
            ret += Parser.toString(many1(alphaNum).parse(scnr));
        } catch (Exception e) {
            // 何もしない
        }
        return ret;
    });

    private static final Parser<BigDecimal> number = apply(toBigDecimal, many1(or(digit, char1('.'))));

    private final Parser<BigDecimal> variable = tryp(scnr -> {
        String varName = identifier.parse(scnr);
        BigDecimal ret = BigDecimal.ZERO;
        try {
            ret = globalScope.get(varName);
        } catch (Exception e) {
            left(e.getMessage()).parse(scnr);
        }
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
    private final Parser<BigDecimal> factor = new Parser<BigDecimal>() {
        @Override
        public BigDecimal parse(Scanner scnr) throws Exception {
            return factor_.parse(scnr);
        }
    };

    // EBNF: term = factor, {("*", factor) | ("/", factor)}
    @SuppressWarnings("unchecked")
    private final Parser<BigDecimal> term = Evaluator.<BigDecimal>eval(factor, many(or(
            char1('*').next(apply((y, z) -> z.multiply(y), factor)),
            char1('/').next(apply((y, z) -> z.divide(y, 20, BigDecimal.ROUND_HALF_UP), factor))
    )));

    // EBNF: expr = term, {("+", term) | ("-", term)}
    @SuppressWarnings("unchecked")
    private final Parser<BigDecimal> expr = Evaluator.<BigDecimal>eval(term, many(or(
            char1('+').next(apply((y, z) -> z.add(y), term)),
            char1('-').next(apply((y, z) -> z.subtract(y), term))
    )));

    @SuppressWarnings("unchecked")
    private final Parser<BigDecimal> function = tryp(scnr -> {
        String funcName = identifier.parse(scnr);
        BigDecimal ret = BigDecimal.ZERO;
        List<BigDecimal> argList = new ArrayList<>();
        argList.add(char1('(').next(expr).parse(scnr));
        argList.addAll(many(char1(',').next(expr)).prev(char1(')')).parse(scnr));
        try {
            ret = BuiltinFunctions.callByString(funcName, argList.toArray(new BigDecimal[0]));
        } catch (Exception e) {
            left(e.getMessage()).parse(scnr);
        }
        return ret;
    });

    // EBNF: factor = [spaces], ("(", expr, ")") | number | variable | function
    @SuppressWarnings("unchecked")
    private final Parser<BigDecimal> factor_ = spaces.next(
            or(char1('(').next(expr).prev(char1(')')), number, variable, function)
    ).prev(spaces);

    // EBNF: assign = [spaces], identifier, [spaces], "=", expr
    @SuppressWarnings("unchecked")
    private final Parser<BigDecimal> assign = tryp(scnr -> {
        String varName = spaces.next(identifier).prev(spaces).parse(scnr);
        BigDecimal value = char1('=').next(expr).parse(scnr);
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
            Parser prsr;
            if (src.contains("=")) {
                prsr = assign;
            } else {
                prsr = expr;
            }
            return prsr.parseToString(scnr);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
