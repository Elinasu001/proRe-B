package com.kh.even.back.file.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachmentDTO {

    private Long fileNo;
    private String originName;
    private String filePath;
    private Date uploadDate;
    private String status;
    
}
