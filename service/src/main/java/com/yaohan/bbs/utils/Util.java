package com.yaohan.bbs.utils;

import java.util.Random;

public class Util {

    public static final Random random = new Random();

    public static int randomInt(int max){
        return random.nextInt(max);
    }
}
