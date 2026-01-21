package com.kh.even.back.admin.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public class AdminMemberDTO {
	    private Long userNo;
	    private String email;
	    private String userName;
	    private String nickname;
	    private String phone;
	    private String birthday;
	    private char gender;
	    private String postcode;
	    private String address;
	    private String addressDetail;
	    private String status;           // Y/N (관리자가 변경 가능)
	    private Date createDate;
	    private Date deleteDate;
	    private Date updateDate;
	    private String userRole;       // ROLE_USER/ROLE_EXPERT/ROLE_ADMIN
	    private String penaltyStatus;    // Y/N (관리자가 변경 가능)
	    
}