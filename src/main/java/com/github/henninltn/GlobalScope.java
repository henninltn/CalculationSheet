package com.github.henninltn;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class GlobalScope {

    public final static BigDecimal e = BigDecimal.valueOf(Math.E);
    public final static BigDecimal pi = BigDecimal.valueOf(Math.PI);
    public final static BigDecimal twoTimesPi = BigDecimal.valueOf(2).multiply(pi);
    public final static BigDecimal halfTimesPi = pi.divide(BigDecimal.valueOf(2));
    public final static BigDecimal three_halvesTimesPi = BigDecimal.valueOf(3).multiply(halfTimesPi);
    public final static BigDecimal positiveInfinity = BigDecimal.valueOf(0.1)
            .multiply(BigDecimal.valueOf(Integer.MAX_VALUE)).multiply(BigDecimal.valueOf(Integer.MAX_VALUE))
            .multiply(BigDecimal.valueOf(Integer.MAX_VALUE)).multiply(BigDecimal.valueOf(Integer.MAX_VALUE))
            .multiply(BigDecimal.valueOf(Integer.MAX_VALUE)).multiply(BigDecimal.valueOf(Integer.MAX_VALUE))
            .multiply(BigDecimal.valueOf(Integer.MAX_VALUE)).multiply(BigDecimal.valueOf(Integer.MAX_VALUE))
            .multiply(BigDecimal.valueOf(Integer.MAX_VALUE)).multiply(BigDecimal.valueOf(Integer.MAX_VALUE))
            .multiply(BigDecimal.valueOf(Integer.MAX_VALUE)).multiply(BigDecimal.valueOf(Integer.MAX_VALUE))
            .multiply(BigDecimal.valueOf(Integer.MAX_VALUE)).multiply(BigDecimal.valueOf(Integer.MAX_VALUE))
            .multiply(BigDecimal.valueOf(Integer.MAX_VALUE)).multiply(BigDecimal.valueOf(Integer.MAX_VALUE))
            .multiply(BigDecimal.valueOf(Integer.MAX_VALUE)).multiply(BigDecimal.valueOf(Integer.MAX_VALUE))
            .multiply(BigDecimal.valueOf(Integer.MAX_VALUE)).multiply(BigDecimal.valueOf(Integer.MAX_VALUE))
            .multiply(BigDecimal.valueOf(Integer.MAX_VALUE)).multiply(BigDecimal.valueOf(Integer.MAX_VALUE))
            .multiply(BigDecimal.valueOf(Integer.MAX_VALUE)).multiply(BigDecimal.valueOf(Integer.MAX_VALUE))
            .multiply(BigDecimal.valueOf(Integer.MAX_VALUE)).multiply(BigDecimal.valueOf(Integer.MAX_VALUE))
            .multiply(BigDecimal.valueOf(Integer.MAX_VALUE)).multiply(BigDecimal.valueOf(Integer.MAX_VALUE))
            .multiply(BigDecimal.valueOf(Integer.MAX_VALUE)).multiply(BigDecimal.valueOf(Integer.MAX_VALUE))
            .multiply(BigDecimal.valueOf(Integer.MAX_VALUE)).multiply(BigDecimal.valueOf(Integer.MAX_VALUE))
            .multiply(BigDecimal.valueOf(Integer.MAX_VALUE)).multiply(BigDecimal.valueOf(Integer.MAX_VALUE));
    public final static BigDecimal negativeInfinity = positiveInfinity.negate();

    private Map<String, BigDecimal> scope;

    public GlobalScope() {
        scope = new HashMap<>();
        scope.put("e", e);
        scope.put("pi", pi);
        scope.put("positiveInfinity", positiveInfinity);
        scope.put("negativeInfinity", negativeInfinity);
    }

    public void put(String key, BigDecimal value) {
        scope.put(key, value);
    }

    public BigDecimal get(String key) throws Exception {
        if (!scope.containsKey(key)) throw new Exception("variable not existed");
        return scope.get(key);
    }
}
