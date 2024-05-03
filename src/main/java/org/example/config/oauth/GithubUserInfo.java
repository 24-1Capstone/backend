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
    public Long getId() {
        return (Long) attributes.get("id");
    }

    @Override
    public String getPassword() {
        return (String) attributes.get("password");
    }

    @Override
    public String getProvider() {
        return "github";
    }


    @Override
    public String getUserName() {
        return (String) attributes.get("login");
    }

    @Override
    public String getAvatarUrl() {
        return (String) attributes.get("avatar_url");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getCompany() {
        return (String) attributes.get("company");
    }

    @Override
    public String getBlog() {
        return (String) attributes.get("blog");
    }

    @Override
    public String getLocation() {
        return (String) attributes.get("location");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }


    @Override
    public boolean isHireable() {
        Object hireableObject = attributes.get("hireable");
        if (hireableObject instanceof Boolean) {
            return (boolean) hireableObject;
        } else {
            return false; // 기본값 설정
        }
    }
    @Override
    public String getBio() {
        return (String) attributes.get("bio");
    }

    @Override
    public String getTwitterUsername() {
        return (String) attributes.get("twitter_username");
    }

    @Override
    public int getPublicRepos() {
        return (int) attributes.get("public_repos");
    }

    @Override
    public int getPublicGists() {
        return (int) attributes.get("public_gists");
    }

    @Override
    public int getFollowers() {
        return (int) attributes.get("followers");
    }

    @Override
    public int getFollowing() {
        return (int) attributes.get("following");
    }

    @Override
    public String getCreatedAt() {
        return (String) attributes.get("created_at");
    }

    @Override
    public String getUpdatedAt() {
        return (String) attributes.get("updated_at");
    }

    @Override
    public int getPrivateGists() {
        return (int) attributes.get("private_gists");
    }

    @Override
    public int getTotalPrivateRepos() {
        return (int) attributes.get("total_private_repos");
    }

    @Override
    public int getOwnedPrivateRepos() {
        return (int) attributes.get("owned_private_repos");
    }

    @Override
    public int getDiskUsage() {
        return (int) attributes.get("disk_usage");
    }

    @Override
    public int getCollaborators() {
        return (int) attributes.get("collaborators");
    }

    @Override
    public boolean isTwoFactorAuthentication() {
        return (boolean) attributes.get("two_factor_authentication");
    }

    @Override
    public String getUrl() {
        return (String) attributes.get("url");
    }

    @Override
    public String getHtmlUrl() {
        return (String) attributes.get("html_url");
    }

    @Override
    public String getFollowersUrl() {
        return (String) attributes.get("followers_url");
    }

    @Override
    public String getFollowingUrl() {
        return (String) attributes.get("following_url");
    }

    @Override
    public String getGistsUrl() {
        return (String) attributes.get("gists_url");
    }

    @Override
    public String getStarredUrl() {
        return (String) attributes.get("starred_url");
    }

    @Override
    public String getSubscriptionsUrl() {
        return (String) attributes.get("subscriptions_url");
    }

    @Override
    public String getOrganizationsUrl() {
        return (String) attributes.get("organizations_url");
    }

    @Override
    public String getReposUrl() {
        return (String) attributes.get("repos_url");
    }

    @Override
    public String getEventsUrl() {
        return (String) attributes.get("events_url");
    }

    @Override
    public String getReceivedEventsUrl() {
        return (String) attributes.get("received_events_url");
    }

    @Override
    public String getType() {
        return (String) attributes.get("type");
    }

    @Override
    public boolean isSiteAdmin() {
        return (boolean) attributes.get("site_admin");
    }
}
