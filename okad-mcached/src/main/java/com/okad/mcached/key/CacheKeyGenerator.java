package com.okad.mcached.key;

import java.lang.reflect.Method;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CacheKeyGenerator implements KeyGenerator {

    private static final String DELIM = ":";

    @Override
    public String generate(Method method, String cache, int[] index, Object... params) {
        StringBuilder sbKey = new StringBuilder();

        if (!StringUtils.isEmpty(cache)) {
            sbKey.append(cache).append(DELIM);
        }

        if (method != null) {
            sbKey.append(method.getName()).append(DELIM);
        }

        if (params == null || params.length == 0 || index == null || index.length == 0) {
            return sbKey.toString();
        }

        sbKey.append(paramsFromIndex(index, params));

        return sbKey.toString();
    }

    @Override
    public String generate(String cache, int[] index, Object... params) {
        return generate(null, cache, index, params);
    }

    private String paramsFromIndex(int[] index, Object... params) {

        StringBuilder sbKey = new StringBuilder();

        for (int i = 0; i < index.length; i++) {

            if (index[i] >= 0 && index[i] < params.length) {

                Object param = params[index[i]];
                if (param != null && !param.getClass().isArray()) {
                    sbKey.append(KeyUtils.keyToString(param)).append(DELIM);
                }
            }
        }

        return sbKey.toString();
    }

}
