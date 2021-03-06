package com.se.pickple_api_server.v1.account.domain.entity;

import com.se.pickple_api_server.v1.account.application.dto.AccountUpdateDto;
import com.se.pickple_api_server.v1.account.domain.type.AccountType;
import com.se.pickple_api_server.v1.account.domain.type.RegisterType;
import com.se.pickple_api_server.v1.common.domain.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class  Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false, unique = true)
    private String idString;

    @Size(min = 2, max = 20)
    @Column(nullable = false)
    private String name;

    // null 허용, 회원수정에서 추가하는 방식
    @Size(min = 8, max = 20)
    @Column
    private String studentId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Size(min = 4, max = 40)
    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private Integer isCertified;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RegisterType registerType;

    @Column(nullable = false)
    private Integer isDeleted;  // 0:존재, 1:삭제


    public Account(String idString, @Size(min = 2, max = 20) String name, AccountType accountType, @Size(min = 4, max = 40) @Email String email, Integer isCertified, RegisterType registerType, Integer isDeleted) {
        this.idString = idString;
        this.name = name;
        this.accountType = accountType;
        this.email = email;
        this.isCertified = isCertified;
        this.registerType = registerType;
        this.isDeleted = isDeleted;
    }

    public void changeAccountInfo(AccountUpdateDto.Request request) {
        this.studentId = request.getNewStudentId();
        this.email = request.getNewEmail();
        this.accountType = AccountType.valueOf(request.getAccountType());
    }

    public void updateIsDeleted(String idString, String email, Integer isDeleted) {
        this.idString = idString;
        this.email = email;
        this.isDeleted = isDeleted;
    }

}