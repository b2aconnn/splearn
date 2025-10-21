package toby.spring.splearn.domain;

public record MemberCreateInfo(
        String email,
        String nickname,
        String password
) {}
