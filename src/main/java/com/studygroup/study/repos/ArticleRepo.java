package com.studygroup.study.repos;

import com.studygroup.study.enteties.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleRepo extends CrudRepository<Article, Long> {
    List<Article> findAllByAuthorId(Long authorId);
}
