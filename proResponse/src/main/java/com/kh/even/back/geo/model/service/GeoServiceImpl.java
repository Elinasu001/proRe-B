package com.kh.even.back.geo.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.kh.even.back.geo.config.KakaoProperties;
import com.kh.even.back.geo.model.dto.GeoCodeResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeoServiceImpl implements GeoService {

    private final RestClient restClient;
    private final KakaoProperties kakaoProperties;

    @Override
    public GeoCodeResponseDTO geocode(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("query는 필수입니다.");
        }

        String q = query.trim();

        /*
         * Kakao 응답 구조
         * documents[0].address.x (경도)
         * documents[0].address.y (위도)
         */
        System.out.println("geocode query = [" + q + "]");
        System.out.println("Kakao REST key length = " + safeLen(kakaoProperties.getRestApiKey()));
        System.out.println("Kakao REST key = [" + maskKey(kakaoProperties.getRestApiKey()) + "]");
        System.out.println("Authorization = [KakaoAK " + maskKey(kakaoProperties.getRestApiKey()) + "]");

        // 1) 주소 검색 API 먼저 시도
        Map body = tryAddressSearch(q);
        System.out.println("Kakao raw response(address) = " + body);

        GeoCodeResponseDTO dto = extractLatLngFromAddressBody(body);
        if (dto != null) {
            return dto;
        }

        // 2) 주소 검색이 비었으면 키워드 검색으로 폴백 (도로명/건물명/가게명 대응)
        //    카카오 keyword query 길이 제한 100이 있으니 방어
        String keywordQuery = (q.length() > 100) ? q.substring(0, 100) : q;

        Map keywordBody = tryKeywordSearch(keywordQuery);
        System.out.println("Kakao raw response(keyword) = " + keywordBody);

        GeoCodeResponseDTO keywordDto = extractLatLngFromKeywordBody(keywordBody);
        if (keywordDto != null) {
            return keywordDto;
        }

        throw new IllegalArgumentException("주소 검색 결과가 없습니다.");
    }

    private Map tryAddressSearch(String query) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("dapi.kakao.com")
                        .path("/v2/local/search/address.json")
                        .queryParam("query", query)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoProperties.getRestApiKey())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Map.class);
    }

    private Map tryKeywordSearch(String query) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("dapi.kakao.com")
                        .path("/v2/local/search/keyword.json")
                        .queryParam("query", query)
                        .queryParam("size", 1) // 1건만 필요
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoProperties.getRestApiKey())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Map.class);
    }

    private GeoCodeResponseDTO extractLatLngFromAddressBody(Map body) {
        Object documentsObj = body.get("documents");
        if (!(documentsObj instanceof List documents) || documents.isEmpty()) {
            return null;
        }

        Object firstObj = documents.get(0);
        if (!(firstObj instanceof Map first)) {
            return null;
        }

        // address 우선, 없으면 road_address fallback
        Map address = (Map) first.get("address");
        if (address == null) {
            address = (Map) first.get("road_address");
        }
        if (address == null) {
            return null;
        }

        Object x = address.get("x"); // 경도
        Object y = address.get("y"); // 위도
        if (x == null || y == null) {
            return null;
        }

        return GeoCodeResponseDTO.builder()
                .lng(String.valueOf(x))
                .lat(String.valueOf(y))
                .build();
    }

    private GeoCodeResponseDTO extractLatLngFromKeywordBody(Map body) {
        Object documentsObj = body.get("documents");
        if (!(documentsObj instanceof List documents) || documents.isEmpty()) {
            return null;
        }

        Object firstObj = documents.get(0);
        if (!(firstObj instanceof Map first)) {
            return null;
        }

        // keyword 응답은 보통 x/y가 최상위에 있음
        Object x = first.get("x"); // 경도
        Object y = first.get("y"); // 위도
        if (x == null || y == null) {
            return null;
        }

        return GeoCodeResponseDTO.builder()
                .lng(String.valueOf(x))
                .lat(String.valueOf(y))
                .build();
    }

    private int safeLen(String s) {
        return (s == null) ? 0 : s.length();
    }

    private String maskKey(String key) {
        if (key == null || key.length() < 8) return String.valueOf(key);
        // 로그에 풀키 찍지 말고 앞/뒤만 남김
        return key.substring(0, 4) + "****" + key.substring(key.length() - 4);
    }
}
