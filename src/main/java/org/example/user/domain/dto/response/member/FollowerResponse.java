package org.example.user.domain.dto.response.member;

import jakarta.persistence.Column;
import lombok.Getter;
import org.example.blog.domain.entity.article.Article;
import org.example.user.domain.entity.member.User;

@Getter
public class FollowerResponse {
    private String id;
    private String username;
    private String avatarUrl;
    private String followers_url;
    private String followings_url;

    public FollowerResponse(User user) {
        this.id = String.valueOf(user.getId());
        this.username = user.getUsername();
        this.avatarUrl = user.getAvatarUrl();
        this.followers_url = user.getFollowersUrl();
        this.followings_url = user.getFollowingsUrl();
    }
}
