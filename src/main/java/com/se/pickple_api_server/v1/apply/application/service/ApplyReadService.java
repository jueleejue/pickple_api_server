package com.se.pickple_api_server.v1.apply.application.service;

import com.se.pickple_api_server.v1.account.application.service.AccountContextService;
import com.se.pickple_api_server.v1.account.domain.entity.Account;
import com.se.pickple_api_server.v1.apply.application.dto.ApplyReadDto;
import com.se.pickple_api_server.v1.apply.application.error.ApplyErrorCode;
import com.se.pickple_api_server.v1.apply.domain.entity.Apply;
import com.se.pickple_api_server.v1.apply.domain.type.ReviewState;
import com.se.pickple_api_server.v1.apply.infra.repository.ApplyJpaRepository;
import com.se.pickple_api_server.v1.apply.infra.repository.ApplyQueryRepository;
import com.se.pickple_api_server.v1.common.application.dto.SearchDto;
import com.se.pickple_api_server.v1.profile.application.error.ProfileErrorCode;
import com.se.pickple_api_server.v1.profile.domain.entity.Profile;
import com.se.pickple_api_server.v1.profile.infra.repository.ProfileJpaRepository;
import com.se.pickple_api_server.v1.recruitment.application.error.BoardErrorCode;
import com.se.pickple_api_server.v1.recruitment.domain.entity.RecruitmentBoard;
import com.se.pickple_api_server.v1.recruitment.infra.repository.RecruitmentBoardJpaRepository;
import com.se.pickple_api_server.v1.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplyReadService {

    private final AccountContextService accountContextService;
    private final ApplyJpaRepository applyJpaRepository;
    private final RecruitmentBoardJpaRepository recruitmentBoardJpaRepository;
    private final ProfileJpaRepository profileJpaRepository;
    private final ApplyQueryRepository applyQueryRepository;


    // ??????????????? ?????? ??? ?????? ??????
    public List<ApplyReadDto.MyResponse> readAllMyApply() {
        Account account = accountContextService.getContextAccount();
        List<Apply> allMyApply = applyJpaRepository.findAllByProfile_AccountAndIsDeletedEquals(account,0);
        List<ApplyReadDto.MyResponse> allMyApplyReadDto
                = allMyApply
                .stream()
                .map(apply -> ApplyReadDto.MyResponse.fromEntity(apply))
                .collect(Collectors.toList());
        return allMyApplyReadDto;
    }

    // ?????? ???????????? ?????? ???????????? ????????? -> ?????? ??????????????? + account?????? ????????? ???????????? ?????????????????? ??????
    public ApplyReadDto.ExistResponse myApplyInRecboard(Long boardId) {
        Account account = accountContextService.getContextAccount();
        RecruitmentBoard recruitmentBoard = recruitmentBoardJpaRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(BoardErrorCode.NO_SUCH_BOARD));
        Apply apply = applyJpaRepository.findByProfile_AccountAndRecruitmentBoard(account, recruitmentBoard)
                .orElseThrow(() -> new BusinessException(ApplyErrorCode.NO_SUCH_APPLY));
        return ApplyReadDto.ExistResponse.fromEntity(apply);
    }

    // ?????? ???????????? ????????? ????????? ??????
    public List<ApplyReadDto.MeResponse> readApplyInRecboard(Long boardId) {
        List<Apply> allApply = applyJpaRepository.findAllByRecruitmentBoard_BoardIdAndIsDeletedEquals(boardId, 0);
        List<ApplyReadDto.MeResponse> allApplyReadDto
                = allApply
                .stream()
                .map(apply -> ApplyReadDto.MeResponse.fromEntity(apply))
                .collect(Collectors.toList());
        return allApplyReadDto;
    }

    // [?????????] ??????????????? ?????? ?????? ????????? (??????)
    public PageImpl readAll(Pageable pageable) {
        Page<Apply> applyPage =  applyJpaRepository.findAll(pageable);
        List<ApplyReadDto.ListResponse> listResponseList = applyPage
                .get()
                .map(apply -> ApplyReadDto.ListResponse.fromEntity(apply))
                .collect(Collectors.toList());
        return new PageImpl(listResponseList, applyPage.getPageable(), applyPage.getTotalElements());
    }


    // ????????? ?????? ????????????
    public ApplyReadDto.Response readApply(Long applyId) {
        Apply apply = applyJpaRepository.findById(applyId)
                .orElseThrow(() -> new BusinessException(ApplyErrorCode.NO_SUCH_APPLY));
        return ApplyReadDto.Response.fromEntity(apply, accountContextService.hasAuthority("ADMIN"));
    }

    // ?????? ???????????? ?????????(???????????? ????????????) ?????? ????????????
    public List<ApplyReadDto.ReviewResponse> readReviewByProfileId(Long profileId) {
        Profile profile = profileJpaRepository.findById(profileId)
                .orElseThrow(() -> new BusinessException(ProfileErrorCode.NO_SUCH_PROFILE));
        List<Apply> applyReviewList = applyJpaRepository.findAllByProfileAndReviewStateEquals(profile, ReviewState.ACCEPT);
        List<ApplyReadDto.ReviewResponse> reviewResponseList = applyReviewList
                .stream()
                .map(review -> ApplyReadDto.ReviewResponse.fromEntity(review))
                .collect(Collectors.toList());
        return reviewResponseList;
    }

    // ?????? ???????????? ??????????????? (?????????)
    public PageImpl search(SearchDto.Apply pageRequest) {
        Page<Apply> applyPage = applyQueryRepository.search(pageRequest);
        List<ApplyReadDto.SListResponse> responseList = applyPage
                .get()
                .map(apply -> ApplyReadDto.SListResponse.fromEntity(apply))
                .collect(Collectors.toList());
        return new PageImpl(responseList, applyPage.getPageable(), applyPage.getTotalElements());
    }

}
