package com.okad.mcached.key;

import java.lang.reflect.Method;

public interface KeyGenerator {

    public String generate(Method method, String cache, int[] index, Object... params);

    public String generate(String cache, int[] index, Object... params);
}
