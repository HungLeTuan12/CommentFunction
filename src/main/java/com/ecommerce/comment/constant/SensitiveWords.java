package com.ecommerce.comment.constant;

import java.util.HashSet;
import java.util.Set;

public class SensitiveWords {
    private static final Set<String> SENSITIVE_WORDS = new HashSet<>();
    static {
        SENSITIVE_WORDS.add("dm");
        SENSITIVE_WORDS.add("giết");
        SENSITIVE_WORDS.add("bắc kỳ");
        SENSITIVE_WORDS.add("nam kỳ");
    }
    public static Set<String> getSensitiveWords() {
        return SENSITIVE_WORDS;
    }
}
