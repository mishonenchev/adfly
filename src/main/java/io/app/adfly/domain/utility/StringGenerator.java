package io.app.adfly.domain.utility;

import java.util.Random;

public class StringGenerator {
    private static String chars = "123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    public static String Generate(int length){
        var charLength = chars.length();
        String result = "";
        for (int i = 0; i < length; i++) {
            var number = (int)(Math.random()*charLength);
            result += chars.charAt(number);
        }
        return result;
    }
}
