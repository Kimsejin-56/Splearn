package spring.splearn.adapter.webapi;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.json.JsonPathValueAssert;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import spring.splearn.AssertThatUtils;
import spring.splearn.adapter.webapi.dto.MemberRegisterResponse;
import spring.splearn.application.member.provided.MemberRegister;
import spring.splearn.application.member.required.MemberRepository;
import spring.splearn.domain.member.Member;
import spring.splearn.domain.member.MemberFixture;
import spring.splearn.domain.member.MemberRegisterRequest;
import spring.splearn.domain.member.MemberStatus;
import tools.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static spring.splearn.AssertThatUtils.equalsTo;
import static spring.splearn.AssertThatUtils.notNull;
import static spring.splearn.domain.member.MemberFixture.createMemberRegisterRequest;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor
public class MemberApiTest {
    final MockMvcTester mvcTester;
    final ObjectMapper objectMapper;
    final MemberRepository memberRepository;
    final MemberRegister memberRegister;

    @Test
    void register() throws UnsupportedEncodingException {
        MemberRegisterRequest request = createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcTestResult result =
                mvcTester.post().uri("/api/members").contentType(APPLICATION_JSON).content(requestJson).exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.memberId", notNull())
                .hasPathSatisfying("$.email", equalsTo(request));

        MemberRegisterResponse response =
                objectMapper.readValue(result.getResponse().getContentAsString(), MemberRegisterResponse.class);
        Member member = memberRepository.findById(response.memberId()).orElseThrow();

        assertThat(member.getEmail().address()).isEqualTo(request.email());
        assertThat(member.getNickname()).isEqualTo(request.nickname());
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmail(){
        memberRegister.register(createMemberRegisterRequest());

        MemberRegisterRequest request = createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcTestResult result =
                mvcTester.post().uri("/api/members").contentType(APPLICATION_JSON).content(requestJson).exchange();

        assertThat(result)
                .apply(print())
                .hasStatus(HttpStatus.CONFLICT);
    }
}
