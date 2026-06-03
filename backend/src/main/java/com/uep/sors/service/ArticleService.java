package com.uep.sors.service;

import com.uep.sors.entity.Article;
import com.uep.sors.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    public List<Article> getArticlesByCategory(String category) {
        return articleRepository.findByCategory(category);
    }

    public List<Article> getArticlesByOrganization(Long organizationId) {
        return articleRepository.findByOrganizationId(organizationId);
    }

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    public Article updateArticle(Long id, Article updatedArticle) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Article not found"));
        article.setTitle(updatedArticle.getTitle());
        article.setContent(updatedArticle.getContent());
        article.setCategory(updatedArticle.getCategory());
        article.setOrganizationId(updatedArticle.getOrganizationId());
        article.setImageUrl(updatedArticle.getImageUrl());
        article.setAuthorName(updatedArticle.getAuthorName());
        return articleRepository.save(article);
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}