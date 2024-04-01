package org.example.config.oauth;

public interface OAuth2UserInfo {
    String getProviderId();

    String getProvider();

    String getUsername();

    String getAvatarUrl();

}
