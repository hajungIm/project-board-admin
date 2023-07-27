package com.springstudy.projectboardadmin.controller;

import com.springstudy.projectboardadmin.config.TestSecurityConfig;
import com.springstudy.projectboardadmin.dto.ArticleDto;
import com.springstudy.projectboardadmin.dto.UserAccountDto;
import com.springstudy.projectboardadmin.service.ArticleManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("컨트롤러 - 게시글 관리")
@Import(TestSecurityConfig.class)
@WebMvcTest(ArticleManagementController.class)
class ArticleManagementControllerTest {

    private final MockMvc mvc;

    @MockBean private ArticleManagementService articleManagementService;

    ArticleManagementControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 관리 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleManagementView_thenReturnsArticleManagementView() throws Exception {
        // Given
        given(articleManagementService.getArticles()).willReturn(List.of());

        // When
        mvc.perform(get("/management/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/articles"))
                .andExpect(model().attribute("articles", List.of()));
        then(articleManagementService).should().getArticles();

        // Then

    }

    @DisplayName("[data][GET] 게시글 1개 - 정상 호출")
    @Test
    void givenArticleId_whenRequestingArticle_thenReturnsArticle() throws Exception {
        // Given
        Long articleId = 1L;
        ArticleDto articleDto = createArticleDto("title", "content");
        given(articleManagementService.getArticle(articleId)).willReturn(articleDto);

        // When
        mvc.perform(get("/management/articles" + articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(jsonPath("$.id").value(articleId))
                .andExpect(jsonPath("$.title").value(articleDto.title()))
                .andExpect(jsonPath("$.content").value(articleDto.content()))
                .andExpect(jsonPath("$.userAccount.nickname").value(articleDto.userAccount().nickname()));
        then(articleManagementService).should().getArticle(articleId);

        // Then

    }

    @DisplayName("[view][POST] 게시글 삭제 - 정상 호출")
    @Test
    void givenArticleId_whenRequestingDeletion_thenRedirectsToArticleManagementView() throws Exception {
        // Given
        Long articleId = 1L;
        willDoNothing().given(articleManagementService).deleteArticle(articleId);

        // When
        mvc.perform(
                post("/management/articles" + articleId)
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/management/articles"))
                .andExpect(redirectedUrl("/management/articles"));
        then(articleManagementService).should().deleteArticle(articleId);
        // Then

    }

    private ArticleDto createArticleDto(String title, String content) {
        return ArticleDto.of(
                1L,
                createUserAccountDto(),
                title,
                content,
                null,
                LocalDateTime.now(),
                "Ihj",
                LocalDateTime.now(),
                "Ihj"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "ihjTest",
                "ihj@email.com",
                "ihj",
                "memo"
        );
    }

}