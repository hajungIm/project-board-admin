package com.springstudy.projectboardadmin.service;

import com.springstudy.projectboardadmin.domain.AdminAccount;
import com.springstudy.projectboardadmin.domain.constant.RoleType;
import com.springstudy.projectboardadmin.dto.AdminAccountDto;
import com.springstudy.projectboardadmin.repository.AdminAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 어드민 회원")
@ExtendWith(MockitoExtension.class)
class AdminAccountServiceTest {

    @InjectMocks private AdminAccountService sut;

    @Mock private AdminAccountRepository adminAccountRepository;


    @DisplayName("존재하는 회원 ID를 검색하면, 회원 데이터를 Optional로 반환한다.")
    @Test
    void givenExistentUserId_whenSearching_thenReturnsOptionalUserData() {
        // Given
        String username = "ihj";
        given(adminAccountRepository.findById(username)).willReturn(Optional.of(createAdminAccount(username)));

        // When
        Optional<AdminAccountDto> result = sut.searchUser(username);

        // Then
        assertThat(result).isPresent();
        then(adminAccountRepository).should().findById(username);
    }

    @DisplayName("존재하지 않는 회원 ID를 검색하면, 비어있는 Optional을 반환한다.")
    @Test
    void givenNonexistentUserId_whenSearching_thenReturnsOptionalUserData() {
        // Given
        String username = "wrong-user";
        given(adminAccountRepository.findById(username)).willReturn(Optional.empty());

        // When
        Optional<AdminAccountDto> result = sut.searchUser(username);

        // Then
        assertThat(result).isEmpty();
        then(adminAccountRepository).should().findById(username);
    }

    @DisplayName("회원 정보를 입력하면, 새로운 회원 정보를 저장하여 가입시키고 해당 회원 데이터를 리턴한다.")
    @Test
    void givenUserParams_whenSaving_thenSavesAdminAccount() {
        // Given
        AdminAccount adminAccount = createSigningUpAdminAccount("ihj", Set.of(RoleType.USER));
        given(adminAccountRepository.save(adminAccount)).willReturn(adminAccount);

        // When
        AdminAccountDto result = sut.saveUser(
                adminAccount.getUserId(),
                adminAccount.getUserPassword(),
                adminAccount.getRoleTypes(),
                adminAccount.getEmail(),
                adminAccount.getNickname(),
                adminAccount.getMemo()
        );

        // Then
        assertThat(result)
                .hasFieldOrPropertyWithValue("userId", adminAccount.getUserId())
                .hasFieldOrPropertyWithValue("userPassword", adminAccount.getUserPassword())
                .hasFieldOrPropertyWithValue("roleTypes", adminAccount.getRoleTypes())
                .hasFieldOrPropertyWithValue("email", adminAccount.getEmail())
                .hasFieldOrPropertyWithValue("nickname", adminAccount.getNickname())
                .hasFieldOrPropertyWithValue("memo", adminAccount.getMemo())
                .hasFieldOrPropertyWithValue("createdBy", adminAccount.getCreatedBy())
                .hasFieldOrPropertyWithValue("modifiedBy", adminAccount.getUserId());
        then(adminAccountRepository).should().save(adminAccount);
    }

    @DisplayName("전체 어드민 회원을 조회한다.")
    @Test
    void givenNothing_whenSelectingAdminAccounts_thenReturnsAdminAccounts() {
        // Given
        given(adminAccountRepository.findAll()).willReturn(List.of());

        // When
        List<AdminAccountDto> result = sut.users();

        // Then
        assertThat(result).hasSize(0);
        then(adminAccountRepository).should().findAll();
    }

    @DisplayName("회원 ID를 입력하면, 회원을 삭제한다.")
    @Test
    void givenUserId_whenDeleting_thenDeletesAdminAccount() {
        // Given
        String userId = "ihj";
        willDoNothing().given(adminAccountRepository).deleteById(userId);

        // When
        sut.deleteUser(userId);

        // Then
        then(adminAccountRepository).should().deleteById(userId);
    }

    private AdminAccount createAdminAccount(String username) {
        return createAdminAccount(username, Set.of(RoleType.USER), null);
    }

    private AdminAccount createSigningUpAdminAccount(String username, Set<RoleType> roleTypes) {
        return createAdminAccount(username, roleTypes, null);
    }

    private AdminAccount createAdminAccount(String username, Set<RoleType> roleTypes, String createdBy) {
        return AdminAccount.of(
                username,
                "pw",
                roleTypes,
                "ihj@email.com",
                "nickname",
                "memo",
                createdBy
        );
    }

}
