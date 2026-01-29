package com.kh.even.back.util;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import org.springframework.stereotype.Component;
import com.kh.even.back.common.validator.AssertUtil;
import com.kh.even.back.util.model.dto.PageResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PagingExcutor {

	private final Pagenation pagenation;

	/**
	 * 페이징 처리 공통 실행 메소드
	 *
	 * 1. 전체 조회 개수(listCount)를 검증한다.
	 *    0이면 notFoundMessage 예외 발생
	 * 2. pageNo, pageSize, listCount를 기반으로 페이징 파라미터를 생성한다.
	 * 3. 호출한 서비스에서 필요한 추가 파라미터를 paramSetter로 주입한다.
	 * 4. listFetcher를 실행하여 실제 목록 데이터를 조회한다.
	 * 5. PageInfo와 조회 결과를 묶어 PageResponse로 반환한다.
	 *
	 * @param <T>             조회 결과 리스트의 타입
	 * @param pageNo          요청 페이지 번호
	 * @param pageSize        한 페이지당 조회 개수
	 * @param listCount       전체 데이터 개수
	 * @param notFoundMessage 데이터가 없을 경우 사용할 예외 메시지
	 * @param paramSetter     서비스별 추가 파라미터 주입 로직
	 * @param listFetcher     실제 목록 조회 로직 (mapper 호출)
	 * @return PageResponse<T> 페이징 정보와 데이터 목록을 포함한 응답 객체
	 */
	public <T> PageResponse<T> execute(
	        int pageNo,
	        int pageSize,
	        int listCount,
	        String notFoundMessage,
	        Consumer<Map<String, Object>> paramSetter,
	        Function<Map<String, Object>, List<T>> listFetcher
	) {
	    AssertUtil.notFound(listCount, notFoundMessage);

	    Map<String, Object> params = pagenation.pageRequest(pageNo, pageSize, listCount);

	    // 서비스별 파라미터 주입
	    paramSetter.accept(params);

	    List<T> list = listFetcher.apply(params);
	    PageInfo pageInfo = (PageInfo) params.get("pi");

	    return new PageResponse<>(list, pageInfo);
	}
	
}
