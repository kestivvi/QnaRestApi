package com.github.kestivvi.qna_rest_api.domain.question.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.kestivvi.qna_rest_api.domain.question.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findByTitle(String title);

}
