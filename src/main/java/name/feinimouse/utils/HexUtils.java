package name.feinimouse.utils;

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
}