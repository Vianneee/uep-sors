package com.uep.sors.controller;

import com.uep.sors.entity.Article;
import com.uep.sors.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public List<Article> getByCategory(@PathVariable String category) {
        return articleService.getArticlesByCategory(category);
    }

    @GetMapping("/organization/{organizationId}")
    public List<Article> getByOrganization(@PathVariable Long organizationId) {
        return articleService.getArticlesByOrganization(organizationId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PIO', 'EDITOR', 'ADMIN')")
    public Article createArticle(@RequestBody Article article) {
        return articleService.createArticle(article);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PIO', 'EDITOR', 'ADMIN')")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id,
                                                  @RequestBody Article article) {
        return ResponseEntity.ok(articleService.updateArticle(id, article));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PIO', 'EDITOR', 'ADMIN')")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}