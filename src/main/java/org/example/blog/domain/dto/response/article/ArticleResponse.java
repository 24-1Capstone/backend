package org.example.domain.dto.response.article;
import lombok.Getter;
import org.example.domain.entity.article.Article;

@Getter
public class ArticleResponse {

    private final String title;
    private final String content;

    public ArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}