package name.feinimouse.utils;

import java.util.Random;

public class HexUtils {
    public static String byteToHex(byte[] bytes) {
        var buf = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() < 2) {
                buf.append("0");
            }
            buf.append(hex);
        }
        return buf.toString();
    }

    public static byte[] hexToByte(String hex) {
        var bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length() / 2; i++) {
            var sub = hex.substring(2 * i, 2 * i + 2);
            bytes[i] = (byte) Integer.parseInt(sub, 16);
        }
        return bytes;
    }
    
    // 随机生成xx位的随机十六进制数
    public static String randomHexString(int place) {
        if (place <= 0) {
            return "0";
        }
        if (place <= 7) {
            return randomHex(place);
        }
        StringBuilder result = new StringBuilder().append(randomHex(place % 7));
        for (int i = 0; i < place / 7; i++) {
            result.append(randomHex(7));
        }
        return result.toString();
    }
    
    private static String randomHex(int place) {
        if (place <= 0) {
            return "";
        }
        int max = 1;
        for (int i = 0; i < place; i++) {
            max *= 16;
        }
        int min = max / 16;
        int scope = max - min;
        return Integer.toHexString(new Random().nextInt(scope) + min);
    }
}