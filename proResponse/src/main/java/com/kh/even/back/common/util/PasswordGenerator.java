package com.kh.even.back.common.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {

  private static final SecureRandom R = new SecureRandom();

  private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  private static final String DIGITS  = "0123456789";
  // 공백 없는 특수문자만
  private static final String SPECIALS = "!@#$%^&*_-+=?";

  public static String generate(int length) {
    if (length < 12) { 
      throw new IllegalArgumentException("length must be >= 12");
    }

    List<Character> chars = new ArrayList<>(length);

    // 1) 필수 조건 3종 1개씩
    chars.add(randomChar(LETTERS));
    chars.add(randomChar(DIGITS));
    chars.add(randomChar(SPECIALS));

    // 2) 나머지 채우기 (letters/digits/specials 섞어서)
    String all = LETTERS + DIGITS + SPECIALS;
    while (chars.size() < length) {
      chars.add(randomChar(all));
    }

    // 3) 셔플 (패턴 고정 방지)
    Collections.shuffle(chars, R);

    // 4) 문자열로 변환
    StringBuilder sb = new StringBuilder(length);
    for (char c : chars) sb.append(c);

    return sb.toString();
  }

  private static char randomChar(String src) {
    return src.charAt(R.nextInt(src.length()));
  }
}

