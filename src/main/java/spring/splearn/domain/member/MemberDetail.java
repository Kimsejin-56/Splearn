package spring.splearn.domain.member;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;
import spring.splearn.domain.AbstractEntity;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail extends AbstractEntity {
    private Profile profile;

    private String introduction;

    private LocalDate registeredAt;

    private LocalDate activatedAt;

    private LocalDate deactivatedAt;

    static MemberDetail create() {
        MemberDetail memberDetail = new MemberDetail();
        memberDetail.registeredAt=LocalDate.now();

        return memberDetail;
    }

    void activate() {
        Assert.isTrue(activatedAt==null, "이미 activatedAt은 설정되었습니다.");

        this.activatedAt=LocalDate.now();
    }

    void deactivate() {
        Assert.isTrue(deactivatedAt==null, "이미 deactivatedAt은 설정되었습니다.");


        this.deactivatedAt=LocalDate.now();
    }

    public void updateInfo(MemberInfoUpdateRequest updateRequest) {
        profile = new Profile(updateRequest.profileAddress());
        introduction = requireNonNull(updateRequest.introduction());
    }
}
