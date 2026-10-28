package spring.splearn.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.splearn.application.provided.MemberRegister;
import spring.splearn.application.required.EmailSender;
import spring.splearn.application.required.MemberRepository;
import spring.splearn.domain.Member;
import spring.splearn.domain.MemberRegisterRequest;
import spring.splearn.domain.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberRegister {
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        //check

        Member member = Member.register(registerRequest, passwordEncoder);

        memberRepository.save(member);

        emailSender.send(member.getEmail(), "회원 등록 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요");

        return member;
    }
}
