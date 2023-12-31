package com.springstudy.projectboardadmin.controller;

import com.springstudy.projectboardadmin.config.GlobalControllerConfig;
import com.springstudy.projectboardadmin.config.TestSecurityConfig;
import com.springstudy.projectboardadmin.dto.UserAccountDto;
import com.springstudy.projectboardadmin.service.UserAccountManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("컨트롤러 - 회원 관리")
@Import({TestSecurityConfig.class, GlobalControllerConfig.class})
@WebMvcTest(UserAccountManagementController.class)
class UserAccountManagementControllerTest {

    private final MockMvc mvc;

    @MockBean private UserAccountManagementService userAccountManagementService;

    public UserAccountManagementControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[view][GET] 회원 관리 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingUserAccountManagementView_thenReturnsUserAccountManagementView() throws Exception {
        // Given
        given(userAccountManagementService.getUserAccounts()).willReturn(List.of());

        // When
        mvc.perform(get("/management/user-accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/user-accounts"))
                .andExpect(model().attribute("userAccounts", List.of()));

        // Then
        then(userAccountManagementService).should().getUserAccounts();
    }

    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[data][GET] 회원 1개 - 정상 호출")
    @Test
    void givenUserAccountId_whenRequestingUserAccount_thenReturnsUserAccount() throws Exception {
        // Given
        String userId = "ihj";
        UserAccountDto userAccountDto = createUserAccountDto(userId, "Ihj");
        given(userAccountManagementService.getUserAccount(userId)).willReturn(userAccountDto);

        // When
        mvc.perform(get("/management/user-accounts/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.nickname").value(userAccountDto.nickname()));

        // Then
        then(userAccountManagementService).should().getUserAccount(userId);
    }

    @WithMockUser(username = "tester", roles = "MANAGER")
    @DisplayName("[view][POST] 회원 삭제 - 정상 호출")
    @Test
    void givenUserAccountId_whenRequestingDeletion_thenRedirectsToUserAccountManagementView() throws Exception {
        // Given
        String userId = "ihj";
        willDoNothing().given(userAccountManagementService).deleteUserAccount(userId);

        // When
        mvc.perform(
                        post("/management/user-accounts/" + userId)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/management/user-accounts"))
                .andExpect(redirectedUrl("/management/user-accounts"));

        // Then
        then(userAccountManagementService).should().deleteUserAccount(userId);
    }

    private UserAccountDto createUserAccountDto(String userId, String nickname) {
        return UserAccountDto.of(
                userId,
                "ihj@email.com",
                nickname,
                "memo"
        );
    }

}