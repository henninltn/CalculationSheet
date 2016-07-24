package com.github.henninltn;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class GlobalScope {
    private Map<String, BigDecimal> scope;

    public GlobalScope() {
        scope = new HashMap<>();
        scope.put("pi", BigDecimal.valueOf(Math.PI));
        scope.put("e", BigDecimal.valueOf(Math.E));
    }

    public void put(String key, double value) {
        scope.put(key, BigDecimal.valueOf(value));
    }

    public void put(String key, int value) {
        scope.put(key, BigDecimal.valueOf(value));
    }

    public void put(String key, BigDecimal value) {
        scope.put(key, value);
    }

    public BigDecimal get(String key) throws Exception {
        if (!scope.containsKey(key)) throw new Exception("variable not existed");
        return scope.get(key);
    }
}
