package org.example.config.oauth;

public interface OAuth2UserInfo {
    Long getId();

    String getPassword();

    String getProvider();

    String getProviderId();

    String getUserName();

    String getAvatarUrl();

    String getName();

    String getCompany();

    String getBlog();

    String getLocation();

    String getEmail();

    boolean isHireable();

    String getBio();

    String getTwitterUsername();

    int getPublicRepos();

    int getPublicGists();

    int getFollowers();

    int getFollowing();

    String getCreatedAt();

    String getUpdatedAt();

    int getPrivateGists();

    int getTotalPrivateRepos();

    int getOwnedPrivateRepos();

    int getDiskUsage();

    int getCollaborators();

    boolean isTwoFactorAuthentication();

    String getUrl();

    String getHtmlUrl();

    String getFollowersUrl();

    String getFollowingUrl();

    String getGistsUrl();

    String getStarredUrl();

    String getSubscriptionsUrl();

    String getOrganizationsUrl();

    String getReposUrl();

    String getEventsUrl();

    String getReceivedEventsUrl();

    String getType();

    boolean isSiteAdmin();
}
