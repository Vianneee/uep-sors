package com.uep.sors.repository;

import com.uep.sors.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByCategory(String category);
    List<Article> findByOrganizationId(Long organizationId);
    List<Article> findAllByOrderByCreatedAtDesc();
}