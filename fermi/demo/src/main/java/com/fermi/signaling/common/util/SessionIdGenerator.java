package com.fermi.signaling.common.util;

import java.security.SecureRandom;

public class SessionIdGenerator {
    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";
    private static final SecureRandom RND = new SecureRandom();

    // 사람이 읽기 쉬운 10자리(0/O, 1/l 같은 혼동 문자 제거)
    public static String generate(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(RND.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}