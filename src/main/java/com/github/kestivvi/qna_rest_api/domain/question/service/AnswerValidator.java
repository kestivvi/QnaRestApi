package com.github.kestivvi.qna_rest_api.domain.question.service;


import org.springframework.stereotype.Service;

import jakarta.validation.Validator;
import com.github.kestivvi.qna_rest_api.domain.question.model.Answer;
import com.github.kestivvi.qna_rest_api.domain.question.repo.AnswerRepository;
import com.github.kestivvi.qna_rest_api.validation.CustomValidator;
import com.github.kestivvi.qna_rest_api.validation.ValidationService;
import com.github.kestivvi.qna_rest_api.validation.exceptions.DuplicateException;
import com.github.kestivvi.qna_rest_api.validation.exceptions.NotFoundException;

@Service
public class AnswerValidator extends CustomValidator<Answer> implements ValidationService<Answer> {

    private final AnswerRepository repository;

    public AnswerValidator(Validator validator, AnswerRepository repository) {
        super(validator);
        this.repository = repository;
    }

    @Override
    public Answer validateExists(long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(className, id));
    }

    @Override
    public void validateNotDuplicate(Answer answer) {
        repository.findByContent(answer.getContent()).ifPresent(a -> {
            throw new DuplicateException(className, "content", a.getBeginningOfTheMessage(100));
        });
    }

}
