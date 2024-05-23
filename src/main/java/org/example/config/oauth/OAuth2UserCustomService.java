package org.example.config.oauth;
import lombok.RequiredArgsConstructor;
import org.example.user.domain.entity.member.User;
import org.example.user.repository.member.UserRepository;
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
//        System.out.println("userRequest:"+userRequest);
//        System.out.println("getClientRegistraion:"+userRequest.getClientRegistration());  //client에 대한 정보들이 받아짐
//        System.out.println("getAccessToken:"+userRequest.getAccessToken().getTokenValue());
//        System.out.println("getAccessToken:"+userRequest.getAccessToken().getScopes());
//        System.out.println("getAccessToken:"+userRequest.getAccessToken().getTokenType());
//        System.out.println("getAccessToken:"+userRequest.getAccessToken().getExpiresAt());
//        System.out.println("getAccessToken:"+userRequest.getAccessToken().getIssuedAt());
//        System.out.println("getAccessToken:"+userRequest.getAccessToken().getClass());
//        System.out.println("getAttributes:"+super.loadUser(userRequest).getAttributes()); //유저 정보를 받아옴
        saveOrUpdate(user, userRequest);

        return user; //사용자 객체는 식별자, 이름, 이메일, 프로필 사진 링크 등의 정보를 담고 있다
    }

    // ❷ 유저가 있으면 업데이트, 없으면 유저 생성
    private User saveOrUpdate(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
//        System.out.println("getAttributes:" + oAuth2User.getAttributes());
        OAuth2UserInfo oAuth2UserInfo = new GithubUserInfo(oAuth2User.getAttributes());

        String username = oAuth2UserInfo.getUserName();
        String password = passwordEncoder.encode("겟인데어");

        User user = userRepository.findByUsername(username)
                .map(entity -> entity.update(username))
                .orElse(User.builder()
                        .username(username)
                        .avatarUrl(oAuth2UserInfo.getAvatarUrl())
                        .followersUrl(oAuth2UserInfo.getFollowersUrl())
                        .followingsUrl(oAuth2UserInfo.getFollowingUrl())
                        .password(password)
                        .provider(oAuth2UserInfo.getProvider())
                        .providerId(oAuth2UserInfo.getProviderId())
                        .build());

        user.setAccessToken(String.valueOf(userRequest.getAccessToken().getTokenValue()));
//
//        GitHubProfile gitHubProfile = gitHubProfileRepository.findByUserName(username)
//                        .map(entity -> entity.update(username))
//                        .orElse(GitHubProfile.builder()
//
//                                .providerId(oAuth2UserInfo.getProviderId())
//                                .userName(oAuth2UserInfo.getUserName())
//                                .password(passwordEncoder.encode("겟인데어"))
//                                .avatarUrl(oAuth2UserInfo.getAvatarUrl())
//                                .followersUrl(oAuth2UserInfo.getFollowersUrl())
//                                .name(oAuth2UserInfo.getName())
//                                .company(oAuth2UserInfo.getCompany())
//                                .blog(oAuth2UserInfo.getBlog())
//                                .location(oAuth2UserInfo.getLocation())
//                                .email(oAuth2UserInfo.getEmail())
//                                .hireable(oAuth2UserInfo.isHireable())
//                                .bio(oAuth2UserInfo.getBio())
//                                .twitterUsername(oAuth2UserInfo.getTwitterUsername())
//                                .publicRepos(oAuth2UserInfo.getPublicRepos())
//                                .publicGists(oAuth2UserInfo.getPublicGists())
//                                .followers(oAuth2UserInfo.getFollowers())
//                                .following(oAuth2UserInfo.getFollowing())
//                                .createdAt(oAuth2UserInfo.getCreatedAt())
//                                .updatedAt(oAuth2UserInfo.getUpdatedAt())
//                                .privateGists(oAuth2UserInfo.getPrivateGists())
//                                .totalPrivateRepos(oAuth2UserInfo.getTotalPrivateRepos())
//                                .ownedPrivateRepos(oAuth2UserInfo.getOwnedPrivateRepos())
//                                .diskUsage(oAuth2UserInfo.getDiskUsage())
//                                .collaborators(oAuth2UserInfo.getCollaborators())
//                                .twoFactorAuthentication(oAuth2UserInfo.isTwoFactorAuthentication())
//                                .url(oAuth2UserInfo.getUrl())
//                                .htmlUrl(oAuth2UserInfo.getHtmlUrl())
//                                .followersUrl(oAuth2UserInfo.getFollowersUrl())
//                                .followingUrl(oAuth2UserInfo.getFollowingUrl())
//                                .gistsUrl(oAuth2UserInfo.getGistsUrl())
//                                .starredUrl(oAuth2UserInfo.getStarredUrl())
//                                .subscriptionsUrl(oAuth2UserInfo.getSubscriptionsUrl())
//                                .organizationsUrl(oAuth2UserInfo.getOrganizationsUrl())
//                                .reposUrl(oAuth2UserInfo.getReposUrl())
//                                .eventsUrl(oAuth2UserInfo.getEventsUrl())
//                                .receivedEventsUrl(oAuth2UserInfo.getReceivedEventsUrl())
//                                .type(oAuth2UserInfo.getType())
//                                .siteAdmin(oAuth2UserInfo.isSiteAdmin())
//                                .build());
//
//
//        gitHubProfileRepository.save(gitHubProfile);





        return userRepository.save(user);
    }

}




