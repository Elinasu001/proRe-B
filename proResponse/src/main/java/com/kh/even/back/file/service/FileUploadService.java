package com.kh.even.back.file.service;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.file.model.vo.FileVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileUploadService {

	private final S3Service s3Service;

	/**
	 * 파일 업로드 공통 처리 메소드
	 *
	 * 1. 업로드할 파일 목록이 없으면 즉시 종료한다. 2. 각 파일을 S3에 업로드하여 파일 경로를 생성한다. 3. 업로드된 파일 정보를
	 * FileVO 객체로 생성한다. 4. 전달받은 saveCallback을 실행하여 - 서비스 상황에 맞는 DB 저장 로직을 수행한다.
	 *
	 * saveCallback은 메서드 레퍼런스를 통해 전달되며, 실행 시점에 어떤 mapper의 save 메소드가 호출될지는 해당 서비스를
	 * 기준으로 결정된다.
	 *
	 * @param files        업로드할 MultipartFile 목록
	 * @param directory    S3에 저장할 디렉토리명
	 * @param refNo        파일이 참조하는 엔티티의 식별자 (requestNo 등)
	 * @param saveCallback 파일 메타정보를 DB에 저장하는 콜백 함수
	 */
	public void uploadFiles(List<MultipartFile> files, String directory, Long refNo, Consumer<FileVO> saveCallback) {
		if (files == null || files.isEmpty()) {
			return;
		}

		for (MultipartFile file : files) {
			String filePath = s3Service.store(file, directory);

			FileVO vo = FileVO.builder().originName(file.getOriginalFilename()).filePath(filePath).status("Y")
					.reqNo(refNo).build();

			// 전달받은 mapper 저장 로직 실행
			saveCallback.accept(vo);
		}
	}

}
