package com.kh.even.back.common.enums;

public enum MemberStatus {
    ACTIVE('Y', "활성"),
    INACTIVE('N', "비활성");  // 탈퇴 포함

    private final char code;
    private final String description;

    MemberStatus(char code, String description) {
        this.code = code;
        this.description = description;
    }

    public char getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static MemberStatus fromCode(char code) {
        for (MemberStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 회원 상태 코드: " + code);
    }

    public static MemberStatus fromCodeOrNull(char code) {
        for (MemberStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}