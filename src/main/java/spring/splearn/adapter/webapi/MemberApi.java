package spring.splearn.adapter.webapi;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring.splearn.adapter.webapi.dto.MemberRegisterResponse;
import spring.splearn.application.member.provided.MemberRegister;
import spring.splearn.domain.member.Member;
import spring.splearn.domain.member.MemberRegisterRequest;

@RestController
@RequiredArgsConstructor
public class MemberApi {

    private final MemberRegister memberRegister;

    @PostMapping("/api/members")
    public MemberRegisterResponse register(@RequestBody @Valid MemberRegisterRequest request) {
        Member member = memberRegister.register(request);

        return MemberRegisterResponse.of(member);
    }
}
