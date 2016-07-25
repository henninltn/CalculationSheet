package com.github.henninltn;

import static com.github.henninltn.GlobalScope.*;

import java.math.BigDecimal;
import java.util.DoubleSummaryStatistics;

class BuiltinFunctions {

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
            default:
                throw new Exception("such function not existed");
        }
        return ret;
    }

    private static BigDecimal getUnitRadian(BigDecimal x) {
        return x.divideAndRemainder(twoTimesPi)[1];
    }

    private static BigDecimal sin(BigDecimal x) {
        BigDecimal unitRadian = getUnitRadian(x);
        if (unitRadian.compareTo(BigDecimal.ZERO) == 0 || unitRadian.compareTo(pi) == 0) return BigDecimal.ZERO;
        else if (unitRadian.compareTo(halfTimesPi) == 0) return BigDecimal.ONE;
        else if (unitRadian.compareTo(three_halvesTimesPi) == 0) return BigDecimal.ONE.negate();
        return BigDecimal.valueOf(Math.sin(x.doubleValue()));
    }

    private static BigDecimal cos(BigDecimal x) {
        BigDecimal unitRadian = getUnitRadian(x);
        if (unitRadian.compareTo(halfTimesPi) == 0 || unitRadian.compareTo(three_halvesTimesPi) == 0) return BigDecimal.ZERO;
        else if (unitRadian.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ONE;
        else if (unitRadian.compareTo(pi) == 0) return BigDecimal.ONE.negate();
        return BigDecimal.valueOf(Math.sin(x.doubleValue()));
    }

    private static BigDecimal tan(BigDecimal x) {
        BigDecimal unitRadian = getUnitRadian(x);
        if (unitRadian.compareTo(BigDecimal.ZERO) == 0 || unitRadian.compareTo(pi) == 0) return BigDecimal.ZERO;
        else if (unitRadian.compareTo(halfTimesPi) == 0)
            return positiveInfinity;
        else if (unitRadian.compareTo(three_halvesTimesPi) == 0)
            return negativeInfinity;
        return BigDecimal.valueOf(Math.tan(x.doubleValue()));
    }
}
