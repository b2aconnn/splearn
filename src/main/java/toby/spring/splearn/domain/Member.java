package toby.spring.splearn.domain;

import lombok.Getter;
import lombok.ToString;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;
import static toby.spring.splearn.domain.MemberStatus.*;

@Getter
@ToString
public class Member {
    private String email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    /**
     * 1. 빌더 패턴은 특정 상태가 누락되어서 생성이 될 수 있다. 그러면 버그를 찾기 힘들어짐.
     *   ㄴ> 아니면 팩토리 메서드 내부에서만 빌더 패턴을 사용하는 방법도 나쁘지 않아 보임.
     *       왜냐하면 외부에서 빌더 패턴을 사용하면 사용한 곳마다 휴먼 에러의 가능성을 열어둬서 버그가 발생할 확률이 높지만
     *       팩토리 메서드 내부에서 빌더 패턴을 사용하면 특정 클래스에서만 확인하면 되기 떄문에 괜찮긴 할텐데..
     *       그럼 외부에서 빌더 패턴을 사용하지 못 하게 막아야 함. private로 만들어야 함. (근데 어차피 그렇게 해야하긴 함. 정적 팩토리 메서드 사용하면)
     * 2. 생성자를 통해 파라미터로 값을 받으면, 추가가 될 때마다 외부에서 호출하는 곳에서 다 수정이 필요하다.
     *   ㄴ> 그래서 이런 상황에서는 파라미터 오브젝트를 만들어서 넘겨주는 것이 더 낫다.
     * 3. 따라서 생성자를 private 으로 막고, 정적 팩토리 메서드를 통해서만 생성하도록 강제한다.
     */
//    private Member(
//            String email,
//            String nickname,
//            String passwordHash
//    ) {
//        this.email = requireNonNull(email);
//        this.nickname = requireNonNull(nickname);
//        this.passwordHash = requireNonNull(passwordHash);
//
//        this.status = PENDING;
//    }

    private Member() {}

    public static Member create(
        MemberCreateInfo createInfo,
        PasswordEncoder passwordEncoder
    ) {
        Member member = new Member();
        member.email = requireNonNull(createInfo.email());
        member.nickname = requireNonNull(createInfo.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(createInfo.password()));

        member.status = PENDING;

        return member;
    }

    public void activate() {
        state(status == PENDING, "Member is not pending");

        this.status = ACTIVE;
    }

    public void deactivate() {
        state(status == ACTIVE, "Member is not active");

        this.status = DEACTIVATED;
    }

    public boolean verifyPassword(
        String password,
        PasswordEncoder passwordEncoder
    ) {
        return passwordEncoder.matches(password, this.passwordHash);
    }
}
