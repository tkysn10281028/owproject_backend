package com.oysterworld.portfolio.owproject_backend.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IdHashUtils {
    public static String hashId(String id) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            var hash = digest.digest(id.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No Algorhithm for Hash -> Error hashing user ID", e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        var hexString = new StringBuilder();
        for (byte b : hash) {
            var hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
