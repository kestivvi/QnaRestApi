package com.github.kestivvi.qna_rest_api.domain.question.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.kestivvi.qna_rest_api.domain.question.model.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findByContent(String content);

}
