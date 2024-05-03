//package org.example.user.domain.entity.member;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class GitHubProfile {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", updatable = false)
//    private Long id;
//
//    @Column(name = "password")
//    private String password;
//
//    private String provider = "github";
//    private String providerId;
//    private String userName;
//    private String avatarUrl;
//    private String name;
//    private String company;
//    private String blog;
//    private String location;
//    private String email;
//    private boolean hireable;
//    private String bio;
//    private String twitterUsername;
//    private int publicRepos;
//    private int publicGists;
//    private int followers;
//    private int following;
//    private String createdAt;
//    private String updatedAt;
//    private int privateGists;
//    private int totalPrivateRepos;
//    private int ownedPrivateRepos;
//    private int diskUsage;
//    private int collaborators;
//    private boolean twoFactorAuthentication;
//
//    private String url;
//    private String htmlUrl;
//    private String followersUrl;
//    private String followingUrl;
//    private String gistsUrl;
//    private String starredUrl;
//    private String subscriptionsUrl;
//    private String organizationsUrl;
//    private String reposUrl;
//    private String eventsUrl;
//    private String receivedEventsUrl;
//    private String type;
//    private boolean siteAdmin;
//
//    @Builder
//    public GitHubProfile(Long id, String password, String providerId, String userName, String avatarUrl, String name,
//                         String company, String blog, String location, String email, boolean hireable, String bio,
//                         String twitterUsername, int publicRepos, int publicGists, int followers, int following,
//                         String createdAt, String updatedAt, int privateGists, int totalPrivateRepos,
//                         int ownedPrivateRepos, int diskUsage, int collaborators, boolean twoFactorAuthentication,
//                         String url, String htmlUrl, String followersUrl, String followingUrl, String gistsUrl,
//                         String starredUrl, String subscriptionsUrl, String organizationsUrl, String reposUrl,
//                         String eventsUrl, String receivedEventsUrl, String type, boolean siteAdmin) {
//        this.id = id;
//        this.password = password;
//        this.providerId = providerId;
//        this.userName = userName;
//        this.avatarUrl = avatarUrl;
//        this.name = name;
//        this.company = company;
//        this.blog = blog;
//        this.location = location;
//        this.email = email;
//        this.hireable = hireable;
//        this.bio = bio;
//        this.twitterUsername = twitterUsername;
//        this.publicRepos = publicRepos;
//        this.publicGists = publicGists;
//        this.followers = followers;
//        this.following = following;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//        this.privateGists = privateGists;
//        this.totalPrivateRepos = totalPrivateRepos;
//        this.ownedPrivateRepos = ownedPrivateRepos;
//        this.diskUsage = diskUsage;
//        this.collaborators = collaborators;
//        this.twoFactorAuthentication = twoFactorAuthentication;
//        this.url = url;
//        this.htmlUrl = htmlUrl;
//        this.followersUrl = followersUrl;
//        this.followingUrl = followingUrl;
//        this.gistsUrl = gistsUrl;
//        this.starredUrl = starredUrl;
//        this.subscriptionsUrl = subscriptionsUrl;
//        this.organizationsUrl = organizationsUrl;
//        this.reposUrl = reposUrl;
//        this.eventsUrl = eventsUrl;
//        this.receivedEventsUrl = receivedEventsUrl;
//        this.type = type;
//        this.siteAdmin = siteAdmin;
//    }
//
//    public GitHubProfile update(String userName) {
//        this.userName = userName;
//        return this;
//    }
//
//}
