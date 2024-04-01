package org.example.repository.article;

import org.example.blog.domain.entity.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
