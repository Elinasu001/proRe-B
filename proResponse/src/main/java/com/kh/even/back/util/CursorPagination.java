package com.kh.even.back.util;

import java.util.HashMap;
import java.util.Map;

public class CursorPagination {
    /**
     * 커서 기반 무한 스크롤 페이징
     * @param messageNo 마지막으로 조회한 메시지의 PK (null이면 최신부터)
     * @param size 한 번에 가져올 데이터 개수
     * @return offset/limit 대신 사용할 커서 기반 파라미터
     */
    public static Map<String, Object> getCursorParams(Long messageNo, int size) {
        Map<String, Object> map = new HashMap<>();
        map.put("messageNo", messageNo);
        map.put("size", size);
        return map;
    }
}
