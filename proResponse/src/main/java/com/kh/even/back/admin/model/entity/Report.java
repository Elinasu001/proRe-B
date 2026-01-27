package com.kh.even.back.admin.model.entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_REPORT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_seq")
    @SequenceGenerator(name = "report_seq", sequenceName = "SEQ_REPORT_NO", allocationSize = 1)
    @Column(name = "REPORT_NO")
    private Long reportNo;

    @Column(name = "CONTENT", length = 2000, nullable = false)
    private String content;

    @Column(name = "CREATE_DATE", nullable = false, updatable = false)
    private LocalDate createDate;

    @Column(name = "UPDATE_DATE")
    private LocalDate updateDate;

    @Column(name = "STATUS", length = 30, nullable = false)
    private String status;

    @Column(name = "REPORTER_USER_NO", nullable = false)
    private Long reporterUserNo;

    @Column(name = "TARGET_USER_NO", nullable = false)
    private Long targetUserNo;

    @Column(name = "REASON_NO", nullable = false)
    private Integer reasonNo;

    @Column(name = "ESTIMATE_NO", nullable = false)
    private Long estimateNo;

    @Column(name = "ANSWER", length = 2000)
    private String answer;

    @Column(name = "ANSWER_DATE")
    private LocalDate answerDate;

    // 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REASON_NO", insertable = false, updatable = false)
    private ReportTag reportTag;

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDate.now();
        if (this.status == null) {
            this.status = "WAITING";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDate.now();
    }

    // 비즈니스 메서드: 신고 상태 변경
    public void updateStatus(String newStatus, String answer) {
        this.status = newStatus;
        this.answer = answer;
        this.answerDate = LocalDate.now();
        this.updateDate = LocalDate.now();
    }

    // 비즈니스 메서드: 답변만 수정
    public void updateAnswer(String answer) {
        this.answer = answer;
        this.answerDate = LocalDate.now();
        this.updateDate = LocalDate.now();
    }
}