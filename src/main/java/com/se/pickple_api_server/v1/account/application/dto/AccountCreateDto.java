package com.se.pickple_api_server.v1.account.application.dto;

import com.se.pickple_api_server.v1.account.domain.entity.AccountType;
import com.se.pickple_api_server.v1.account.domain.entity.RegisterType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.*;

import javax.validation.constraints.Size;

public class AccountCreateDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("소셜로그인 회원정보 등록 요청")
    @Builder
    static public class Request {

        @ApiModelProperty(example = "account", notes = "아이디")
        private String id;

        @ApiModelProperty(example = "name", notes = "이름")
        @Size(min = 2, max = 20)
        private String name;

        @ApiModelProperty(example = "20170000", notes = "학번")
        @Size(min=8, max=20)
        private String studentId;

        @ApiModelProperty(example = "MEMBER", notes = "사용자 타입")
        private AccountType accountType;

        @ApiModelProperty(example = "01044445555", notes = "전화번호, 01000000000 형식")
        @Size(min = 10, max = 20)
        private String phoneNumber;

        @ApiModelProperty(example = "test@naver.com", notes = "이메일")
        @Size(min = 4, max = 40)
        private String email;

        @ApiModelProperty(example = "0", notes = "금오공대생 인증 여부")
        private Integer isCertified;

        @ApiModelProperty(example = "PICKPLE", notes = "사용자 유입 경로")
        private RegisterType registerType;

        @ApiModelProperty(example = "0", notes = "사용자 탈퇴 여부")
        private Integer isDeleted;

    }

    @Data
    @AllArgsConstructor
    @ApiModel("회원가입 응답")
    static public class Response {
        @ApiModelProperty(example = "1", notes = "사용자 pk")
        private Long id;
    }

}
