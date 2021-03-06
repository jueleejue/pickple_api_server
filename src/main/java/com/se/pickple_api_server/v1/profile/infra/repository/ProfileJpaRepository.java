package com.se.pickple_api_server.v1.profile.infra.repository;

import com.se.pickple_api_server.v1.account.domain.entity.Account;
import com.se.pickple_api_server.v1.profile.domain.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileJpaRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByAccount(Account account);
    Page<Profile> findAllByIsOpenEquals(Pageable pageable, Integer isOpen);
    Optional<Profile> findByKakaoId(String kakaoId);
    Optional<Profile> findByWorkEmail(String workEmail);
    Optional<Profile> findByBlog(String blog);

}
