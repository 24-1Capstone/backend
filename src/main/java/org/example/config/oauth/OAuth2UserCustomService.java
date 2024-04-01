package org.example.config.oauth;
import lombok.RequiredArgsConstructor;
import org.example.member.domain.User;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest); // ❶ 요청을 바탕으로 유저 정보를 담은 객체 반환
        saveOrUpdate(user);

        return user; //사용자 객체는 식별자, 이름, 이메일, 프로필 사진 링크 등의 정보를 담고 있다
    }

    // ❷ 유저가 있으면 업데이트, 없으면 유저 생성
    private User saveOrUpdate(OAuth2User oAuth2User) {
        System.out.println("getAttributes:" + oAuth2User.getAttributes());
        OAuth2UserInfo oAuth2UserInfo = new GithubUserInfo(oAuth2User.getAttributes());

        String provider = oAuth2UserInfo.getProvider(); //github
        String providerId = oAuth2UserInfo.getProviderId(); //githubID
        String username = oAuth2UserInfo.getUsername();
        String password = passwordEncoder.encode("겟인데어");
        String avatarUrl = oAuth2UserInfo.getAvatarUrl();

        User user = userRepository.findByUsername(username)
                .map(entity -> entity.update(username))
                .orElse(User.builder()
                        .username(username)
                        .avatarUrl(avatarUrl)
                        .password(password)
                        .provider(provider)
                        .providerId(providerId)
                        .build());

        return userRepository.save(user);
    }

}