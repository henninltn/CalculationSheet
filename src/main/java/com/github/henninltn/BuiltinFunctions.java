package com.github.henninltn;

import static com.github.henninltn.GlobalScope.*;

import java.math.BigDecimal;

/**
 * 組み込みの関数を提供
 */
class BuiltinFunctions {

    /**
     * 組み込み関数を文字列で指定して呼び出す
     * @param funcName
     * @param xs
     * @return
     * @throws Exception
     */
    public static BigDecimal callByString(String funcName, BigDecimal... xs) throws Exception {
        BigDecimal ret = BigDecimal.ZERO;
        switch (funcName) {
            case "sin":
                System.out.println("sin");
                if (xs.length != 1) throw new Exception("invalid number of arguments");
                ret = sin(xs[0]);
                break;
            case "cos":
                System.out.println("cos");
                if (xs.length != 1) throw new Exception("invalid number of arguments");
                ret = cos(xs[0]);
                break;
            case "tan":
                if (xs.length != 1) throw new Exception("invalid number of arguments");
                ret = tan(xs[0]);
                break;
            case "log":
                if (xs.length != 1) throw new Exception("invalid number of arguments");
                ret = log(xs[0]);
                break;
            case "exp":
                if (xs.length != 1) throw new Exception("invalid number of arguments");
                ret = log(xs[0]);
                break;
            default:
                throw new Exception("such function not existed");
        }
        return ret;
    }

    private static BigDecimal getUnitRadian(BigDecimal x) {
        return x.divideAndRemainder(twoTimesPi)[1];
    }

    /**
     * Sin関数
     * @param x
     * @return
     */
    private static BigDecimal sin(BigDecimal x) {
        // そのままMath.sin関数を使うとdouble値のずれによって正しい値が出ないため調整
        BigDecimal unitRadian = getUnitRadian(x);
        if (unitRadian.compareTo(BigDecimal.ZERO) == 0 || unitRadian.compareTo(pi) == 0) return BigDecimal.ZERO;
        else if (unitRadian.compareTo(halfTimesPi) == 0) return BigDecimal.ONE;
        else if (unitRadian.compareTo(three_halvesTimesPi) == 0) return BigDecimal.ONE.negate();
        return BigDecimal.valueOf(Math.sin(x.doubleValue()));
    }

    /**
     * Cos関数
     * @param x
     * @return
     */
    private static BigDecimal cos(BigDecimal x) {
        // そのままMath.sin関数を使うとdouble値のずれによって正しい値が出ないため調整
        BigDecimal unitRadian = getUnitRadian(x);
        if (unitRadian.compareTo(halfTimesPi) == 0 || unitRadian.compareTo(three_halvesTimesPi) == 0) return BigDecimal.ZERO;
        else if (unitRadian.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ONE;
        else if (unitRadian.compareTo(pi) == 0) return BigDecimal.ONE.negate();
        return BigDecimal.valueOf(Math.sin(x.doubleValue()));
    }

    /**
     * Tan関数
     * @param x
     * @return
     */
    private static BigDecimal tan(BigDecimal x) {
        // そのままMath.sin関数を使うとdouble値のずれによって正しい値が出ないため調整
        BigDecimal unitRadian = getUnitRadian(x);
        if (unitRadian.compareTo(BigDecimal.ZERO) == 0 || unitRadian.compareTo(pi) == 0) return BigDecimal.ZERO;
        else if (unitRadian.compareTo(halfTimesPi) == 0)
            return positiveInfinity;
        else if (unitRadian.compareTo(three_halvesTimesPi) == 0)
            return negativeInfinity;
        return BigDecimal.valueOf(Math.tan(x.doubleValue()));
    }

    /**
     * 対数関数
     * @param x
     * @return
     */
    private static BigDecimal log(BigDecimal x) {
        // そのままMath.sin関数を使うとdouble値のずれによって正しい値が出ないため調整
        if (x.compareTo(BigDecimal.ZERO) == 0) return negativeInfinity;
        return BigDecimal.valueOf(Math.log(x.doubleValue()));
    }

    /**
     * 指数関数
     * @param x
     * @return
     */
    private static BigDecimal exp(BigDecimal x) {
        return BigDecimal.valueOf(Math.exp(x.doubleValue()));
    }
}
