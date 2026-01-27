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
	 * 
	 * @param files 이미지가 담긴 파일 리스트
	 * @param directory 저장한 디렉토리 명
	 * @param requestNo 테이블에 참조하는 No
	 * @param saveCallback mapper 레퍼런스 메소드
	 */
	public void uploadFiles(List<MultipartFile> files, String directory, Long refNo,
			Consumer<FileVO> saveCallback) {
		if (files == null || files.isEmpty()) {
			return;
		}

		for (MultipartFile file : files) {
			String filePath = s3Service.store(file, directory);

			FileVO vo = FileVO.builder().originName(file.getOriginalFilename()).filePath(filePath).status("Y").reqNo(refNo)
					.build();
			
			//Consumer<FileVO> saveCallback = vo -> mapper.saveExpertEstimateAttachment(vo); 컴파일 시점에서 바뀌는값 뒤에 mapper.save 는 바뀔수있음
			saveCallback.accept(vo);
		}
	}
}
