package org.example.config.oauth;

import java.util.Map;

public class GithubUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    public GithubUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        Object idObject = attributes.get("id");
        if (idObject != null) {
            return String.valueOf(idObject); // Integer를 String으로 안전하게 변환
        } else {
            return null; // 또는 적절한 기본값이나 예외 처리
        }
    }

    @Override
    public String getProvider() {
        return "github";
    }

    @Override
    public String getUsername() {
        return (String) attributes.get("login");
    }

    @Override
    public String getAvatarUrl() {
        return (String) attributes.get("avatar_url");
    }

    @Override
    public String getFollowersUrl() {
        return (String) attributes.get("followers_url");
    }
}
