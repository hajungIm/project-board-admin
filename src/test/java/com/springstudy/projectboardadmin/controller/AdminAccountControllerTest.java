package com.springstudy.projectboardadmin.controller;

import com.springstudy.projectboardadmin.config.SecurityConfig;
import com.springstudy.projectboardadmin.config.TestSecurityConfig;
import com.springstudy.projectboardadmin.domain.constant.RoleType;
import com.springstudy.projectboardadmin.dto.AdminAccountDto;
import com.springstudy.projectboardadmin.service.AdminAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 어드민 회원")
@Import(SecurityConfig.class)
@WebMvcTest(AdminAccountController.class)
class AdminAccountControllerTest {

    private final MockMvc mvc;

    @MockBean private AdminAccountService adminAccountService;

    AdminAccountControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @BeforeTestMethod
    public void securitySetup() {
        given(adminAccountService.searchUser(anyString()))
                .willReturn(Optional.of(createAdminAccountDto()));
        given(adminAccountService.saveUser(anyString(), anyString(), anySet(), anyString(), anyString(), anyString()))
                .willReturn(createAdminAccountDto());
    }


    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[view][GET] 어드민 회원 페이지 - 정상 호출")
    @Test
    void givenAuthorizedUser_whenRequestingAdminMembersView_thenReturnsAdminMembersView() throws Exception {
        // Given

        // When
        mvc.perform(get("/admin/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/members"));

        // Then

    }

    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[data][GET] 어드민 회원 리스트 - 정상 호출")
    @Test
    void givenAuthorizedUser_whenRequestingAdminMembers_thenReturnsAdminMembers() throws Exception {
        // Given
        given(adminAccountService.users()).willReturn(List.of());

        // When
        mvc.perform(get("/api/admin/members"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // Then
        then(adminAccountService).should().users();
    }

    @WithMockUser(username = "tester", roles = "MANAGER")
    @DisplayName("[data][DELETE] 어드민 회원 삭제 - 정상 호출")
    @Test
    void givenAuthorizedUser_whenDeletingAdminMember_thenDeletesAdminMember() throws Exception {
        // Given
        String username = "ihj";
        willDoNothing().given(adminAccountService).deleteUser(username);

        // When
        mvc.perform(
                        delete("/api/admin/members/" + username)
                                .with(csrf())
                )
                .andExpect(status().isNoContent());

        // Then
        then(adminAccountService).should().deleteUser(username);
    }

    private AdminAccountDto createAdminAccountDto() {
        return AdminAccountDto.of(
                "ihjTest",
                "pw",
                Set.of(RoleType.USER),
                "ihj@email.com",
                "IHJTEST",
                "memo"
        );
    }
}
