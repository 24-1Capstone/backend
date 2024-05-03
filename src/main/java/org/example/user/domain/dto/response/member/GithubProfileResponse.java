package org.example.user.domain.dto.response.member;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class GithubProfileResponse {
    private Long id;
    private String password;
    private String followersUrl;
    private String provider;
    private String providerId;
    private String userName;
    private String avatarUrl;
    private String name;
    private String company;
    private String blog;
    private String location;
    private String email;
    private boolean hireable;
    private String bio;
    private String twitterUsername;
    private int publicRepos;
    private int publicGists;
    private int followers;
    private int following;
    private String createdAt;
    private String updatedAt;
    private int privateGists;
    private int totalPrivateRepos;
    private int ownedPrivateRepos;
    private int diskUsage;
    private int collaborators;
    private boolean twoFactorAuthentication;
    private String url;
    private String htmlUrl;
    private String followingUrl;
    private String gistsUrl;
    private String starredUrl;
    private String subscriptionsUrl;
    private String organizationsUrl;
    private String reposUrl;
    private String eventsUrl;
    private String receivedEventsUrl;
    private String type;
    private boolean siteAdmin;

//    public GithubProfileResponse(GitHubProfile gitHubProfile) {
//        this.id = gitHubProfile.getId();
//        this.password = gitHubProfile.getPassword();
//        this.followersUrl = gitHubProfile.getFollowersUrl();
//        this.provider = gitHubProfile.getProvider();
//        this.providerId = gitHubProfile.getProviderId();
//        this.userName = gitHubProfile.getUserName();
//        this.avatarUrl = gitHubProfile.getAvatarUrl();
//        this.name = gitHubProfile.getName();
//        this.company = gitHubProfile.getCompany();
//        this.blog = gitHubProfile.getBlog();
//        this.location = gitHubProfile.getLocation();
//        this.email = gitHubProfile.getEmail();
//        this.hireable = gitHubProfile.isHireable();
//        this.bio = gitHubProfile.getBio();
//        this.twitterUsername = gitHubProfile.getTwitterUsername();
//        this.publicRepos = gitHubProfile.getPublicRepos();
//        this.publicGists = gitHubProfile.getPublicGists();
//        this.followers = gitHubProfile.getFollowers();
//        this.following = gitHubProfile.getFollowing();
//        this.createdAt = gitHubProfile.getCreatedAt();
//        this.updatedAt = gitHubProfile.getUpdatedAt();
//        this.privateGists = gitHubProfile.getPrivateGists();
//        this.totalPrivateRepos = gitHubProfile.getTotalPrivateRepos();
//        this.ownedPrivateRepos = gitHubProfile.getOwnedPrivateRepos();
//        this.diskUsage = gitHubProfile.getDiskUsage();
//        this.collaborators = gitHubProfile.getCollaborators();
//        this.twoFactorAuthentication = gitHubProfile.isTwoFactorAuthentication();
//        this.url = gitHubProfile.getUrl();
//        this.htmlUrl = gitHubProfile.getHtmlUrl();
//        this.followingUrl = gitHubProfile.getFollowingUrl();
//        this.gistsUrl = gitHubProfile.getGistsUrl();
//        this.starredUrl = gitHubProfile.getStarredUrl();
//        this.subscriptionsUrl = gitHubProfile.getSubscriptionsUrl();
//        this.organizationsUrl = gitHubProfile.getOrganizationsUrl();
//        this.reposUrl = gitHubProfile.getReposUrl();
//        this.eventsUrl = gitHubProfile.getEventsUrl();
//        this.receivedEventsUrl = gitHubProfile.getReceivedEventsUrl();
//        this.type = gitHubProfile.getType();
//        this.siteAdmin = gitHubProfile.isSiteAdmin();
//    }
}
