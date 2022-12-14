package chat.Shared.Security;

import java.lang.Math;
import java.math.BigInteger;
import java.security.SecureRandom;

public class BCrypt {

    private static final char[] RADIX64_TABLE = "./ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    private class Blowfish {
        int[] subkeys;
        int[][] sbox;

        public Blowfish(int cost, byte[] salt, byte[] password) {
            subkeys = new int[18];
            sbox = new int[4][256];
            byte[] emptySalt = new byte[16];
            BigInteger totalCost = (new BigInteger("2")).pow(cost);
            for (int i = 0; i < 16; i++) {
                emptySalt[i] = 0;
            }
            initialize();
            expandKey(salt, password);
            for (BigInteger i = new BigInteger("0"); i.compareTo(totalCost) < 0; i = i.add(new BigInteger("1"))) {
                expandKey(emptySalt, password);
                expandKey(emptySalt, salt);
            }
        }

        private void initialize() {
            for (int i = 0; i < 18; i++) {
                subkeys[i] = 
                    getPiHexDigit(i*4)*(int)Math.pow(16, 3) +
                    getPiHexDigit(i*4+1)*(int)Math.pow(16, 2) +
                    getPiHexDigit(i*4+2)*16 +
                    getPiHexDigit(i*4+3);
            }
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 256; j++) {
                    sbox[i][j] = 
                        getPiHexDigit(72+i*1024+j*4)*(int)Math.pow(16, 3) +
                        getPiHexDigit(72+i*1024+j*4+1)*(int)Math.pow(16, 2) +
                        getPiHexDigit(72+i*1024+j*4+2)*16 +
                        getPiHexDigit(72+i*1024+j*4+3);
                }
            }
        }

        private void expandKey(byte[] salt, byte[] password) {
            for (int i = 0; i < 18; i++) {
                subkeys[i] ^=
                    (password[i*4 % password.length] << 24) +
                    (password[(i*4+1) % password.length] << 16) +
                    (password[(i*4+2) % password.length] << 8) +
                    password[(i*4+3) % password.length];
            }
            byte[][] saltHalf = new byte[2][8];
            for (int i = 0; i < 8; i++) {
                saltHalf[0][i] = salt[i];
                saltHalf[1][i] = salt[i+8];
            }
            byte[] block = new byte[8];
            for (int i = 0; i < 8; i++) {
                block[i] = 0;
            }
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 8; j++) {
                    block[j] ^= saltHalf[i%2][j];
                }
                encrypt(block);
                subkeys[2*i] = (block[0] << 24) + (block[1] << 16) + (block[2] << 8) + block[3];
                subkeys[2*i+1] = (block[4] << 24) + (block[5] << 16) + (block[6] << 8) + block[7];
            }
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 128; j++) {
                    for (int k = 0; k < 8; k++) {
                        block[k] ^= saltHalf[j%2][k];
                    }
                    encrypt(block);
                    sbox[i][2*j] = (block[0] << 24) + (block[1] << 16) + (block[2] << 8) + block[3];
                    sbox[i][2*j+1] = (block[4] << 24) + (block[5] << 16) + (block[6] << 8) + block[7];
                }
            }
        }

        public void encrypt(byte[] block) {
            int L = (block[0] << 24) + (block[1] << 16) + (block[2] << 8) + block[3];
            int R = (block[4] << 24) + (block[5] << 16) + (block[6] << 8) + block[7];
            for (int i = 0; i < 16; i++) {
                L ^= subkeys[i];
                R ^= f(L);
                int temp = L;
                L = R;
                R = temp;
            }
            int temp = L;
            L = R;
            R = temp;
            R ^= subkeys[16];
            L ^= subkeys[17];
            block[0] = (byte) (L >>> 24);
            block[1] = (byte) (L >>> 16);
            block[2] = (byte) (L >>> 8);
            block[3] = (byte) L;
            block[4] = (byte) (R >>> 24);
            block[5] = (byte) (R >>> 16);
            block[6] = (byte) (R >>> 8);
            block[7] = (byte) R;
        }

        private int f(int x) {
            int h = sbox[0][x >>> 24] + sbox[1][x >>> 16 & 0xFF];
            return (h ^ sbox[2][x >>> 8 & 0xFF]) + sbox[3][x & 0xFF];
        }

        private int getPiHexDigit(int n) {
            int res = (int) Math.floor(16*((4*sum(n-1, 1)-2*sum(n-1, 4)-sum(n-1, 5)-sum(n-1, 6)) % 1));
            if (res > 0) {
                return res;
            } else {
                return res + 16;
            }
        }

        private int powermod(int x, int y, int p) {
            int res = 1;
            x = x % p;
            while (y > 0) {
                if (y % 2 == 1) {
                    res = (res * x) % p;
                }
                y = y >> 1;
                x = (x * x) % p;
            }
            return res;
        }

        private double sum(int n, int j) {
            double s = 0.0;
            int denom = j;
            for (int i = 0; i <= n; i++) {
                s = (s + (double)(powermod(16, n-i, denom)) / denom) % 1;
                denom += 8;
            }
            double num = 1.0 / 16;
            double frac = num / denom;
            while (frac > Math.ulp(s)) {
                s += frac;
                num /= 16;
                denom += 8;
                frac = num / denom;
            }
            return s % 1;
        }
    }

    public BCrypt() {
        
    }

    private String radix64encode(byte[] input) {
        int bytesRetrieved = 0;
        int bitsInBuffer = 0;
        int buffer = 0;
        String output = "";
        while (true) {
            if (bitsInBuffer < 6) {
                if (bytesRetrieved == input.length) {
                    if (bitsInBuffer == 0) {
                        break;
                    }
                    buffer <<= 6 - bitsInBuffer;
                    bitsInBuffer = 6;
                }
                else {
                    buffer <<= 8;
                    buffer |= 0xff & (input[bytesRetrieved++]);
                    bitsInBuffer += 8;
                }
            }
            int index = 0x3f & (buffer >> (bitsInBuffer - 6));
            bitsInBuffer -= 6;
            output += RADIX64_TABLE[index];
        }
        return output;
    }

    private byte[] radix64decode(String encoded) {
        byte[] result = new byte[encoded.length() * 3 / 4];
        int bytesRetrieved = 0;
        int bitsInBuffer = 0;
        int buffer = 0;
        for (int i = 0; i < encoded.length(); i++) {
            int index = -1;
            for (int j = 0; j < RADIX64_TABLE.length; j++) {
                if (encoded.charAt(i) == RADIX64_TABLE[j]) {
                    index = j;
                    break;
                }
            }
            if (index == -1) {
                throw new IllegalArgumentException("Invalid character in radix64 string");
            }
            buffer <<= 6;
            buffer |= index;
            bitsInBuffer += 6;
            if (bitsInBuffer >= 8) {
                result[bytesRetrieved++] = (byte) (buffer >> (bitsInBuffer - 8));
                bitsInBuffer -= 8;
            }
            if (bytesRetrieved == result.length) {
                break;
            }
        }
        return result;
    }

    private String hash(int cost, byte[] salt, byte[] password) {
        Blowfish bf = new Blowfish(cost, salt, password);
        byte[][] blocks = {
            "OrpheanB".getBytes(),
            "eholderS".getBytes(),
            "cryDoubt".getBytes()
        };
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 3; j++) {
                bf.encrypt(blocks[j]);
            }
        };
        byte[] result = new byte[24];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                result[i*8+j] = blocks[i][j];
            }
        }
        return radix64encode(result);
    }

    private String hash(int cost, String salt, String password) {
        byte[] saltBytes = radix64decode(salt);
        return hash(cost, saltBytes, password.getBytes());
    }

    /** Генерирует хэш для пароля
     * @param cost стоимость вычислений
     * @param password исходная строка пароля
     * @return возвращает хэшированную строку
     */
    public String hash(int cost, String password) {
        byte[] salt = new byte[16];
        (new SecureRandom()).nextBytes(salt);
        return "$2a$" + cost + "$" + radix64encode(salt) + hash(cost, salt, password.getBytes());
    }

    /** Сравнивает пароль и хэш пароля
     * @param password строка пароля
     * @param hash строка хэша
     * @return {@code true} если пароль совпадает, {@code false} в противном случае.
     */
    public boolean compare(String password, String hash) {
        String[] parts = hash.split("\\$");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid hash");
        }
        if (!parts[1].equals("2a")) {
            throw new IllegalArgumentException("Invalid hash");
        }
        int cost = Integer.parseInt(parts[2]);
        String salt = parts[3].substring(0, 22);
        String hash2 = "$2a$" + cost + "$" + salt + hash(cost, salt, password);
        return hash2.equals(hash);
    }

    public static void main(String[] args) {
       BCrypt bcrypt = new BCrypt();
       int cost = 10;
       String password = "Kappapride27@";
       String hash = bcrypt.hash(cost, password);
       System.out.println(hash);
       System.out.println(bcrypt.compare(password, hash));
    }
}
