package com.kh.even.back.admin.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_REPORT_TAG")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReportTag {

    @Id
    @Column(name = "REASON_NO")
    private Integer reasonNo;

    @Column(name = "REASON_NAME", length = 1000, nullable = false)
    private String reasonName;
}