package spring.splearn.adapter.webapi;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import spring.splearn.application.member.provided.MemberRegister;
import spring.splearn.domain.member.Member;
import spring.splearn.domain.member.MemberRegisterRequest;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static spring.splearn.domain.member.MemberFixture.createMember;
import static spring.splearn.domain.member.MemberFixture.createMemberRegisterRequest;

@WebMvcTest(MemberApi.class)
@RequiredArgsConstructor
class MemberApiWebMvcTest {
    final MockMvcTester mvcTester;
    final ObjectMapper objectMapper;

    @MockitoBean
    private MemberRegister memberRegister;

    @Test
    void register(){
        Member member = createMember(1L);
        when(memberRegister.register(any())).thenReturn(member);

        MemberRegisterRequest request = createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        assertThat(mvcTester.post().uri("/api/members").contentType(APPLICATION_JSON)
                .content(requestJson))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.memberId").asNumber().isEqualTo(1);

        verify(memberRegister).register(request);
    }

    @Test
    void registerFail(){
        MemberRegisterRequest request = createMemberRegisterRequest("invalid email");
        String requestJson = objectMapper.writeValueAsString(request);

        assertThat(mvcTester.post().uri("/api/members").contentType(APPLICATION_JSON)
                .content(requestJson))
                .hasStatus(BAD_REQUEST);
    }
}