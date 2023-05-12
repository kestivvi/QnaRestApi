package com.github.kestivvi.qna_rest_api.domain.question.service;


import org.springframework.stereotype.Service;

import com.github.kestivvi.qna_rest_api.domain.question.model.Answer;
import com.github.kestivvi.qna_rest_api.domain.question.model.Question;
import com.github.kestivvi.qna_rest_api.domain.question.model.Tag;
import com.github.kestivvi.qna_rest_api.domain.question.repo.QuestionRepository;
import com.github.kestivvi.qna_rest_api.validation.CustomValidator;
import com.github.kestivvi.qna_rest_api.validation.ValidationService;
import com.github.kestivvi.qna_rest_api.validation.exceptions.DuplicateException;
import com.github.kestivvi.qna_rest_api.validation.exceptions.NotContainsException;
import com.github.kestivvi.qna_rest_api.validation.exceptions.NotFoundException;

import jakarta.validation.Validator;

@Service
public class QuestionValidator extends CustomValidator<Question> implements ValidationService<Question> {

    private final QuestionRepository repository;
    private final AnswerValidator answerValidator;
    private final TagValidator tagValidator;

    public QuestionValidator(Validator validator, QuestionRepository repository, AnswerValidator answerValidator, TagValidator tagValidator) {
        super(validator);
        this.repository = repository;
        this.answerValidator = answerValidator;
        this.tagValidator = tagValidator;
    }

    @Override
    public Question validateExists(long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(className, id));
    }

    @Override
    public void validateNotDuplicate(Question question) {
        repository.findByTitle(question.getTitle()).ifPresent(a -> {
            throw new DuplicateException(className, "title", a.getTitle());
        });
    }

    public Answer validateAnswerExists(long id) {
        return answerValidator.validateExists(id);
    }

    public Tag validateTagExists(long id) {
        return tagValidator.validateExists(id);
    }

    public Question validateQuestionContainsAnswer(Question question, Answer answer) {
        if (question.getAnswers().stream().noneMatch(g -> g.getId().equals(answer.getId())))
            throw new NotContainsException(className, "answer", answer.getBeginningOfTheMessage(100));
        return question;
    }

    public Question validateQuestionContainsTag(Question question, Tag tag) {
        if (question.getTags().stream().noneMatch(t -> t.getId().equals(tag.getId())))
            throw new NotContainsException(className, "tag", tag.getName());
        return question;
    }

    public Question validateQuestionNotContainsTag(Question question, Long id) {
        question.getTags().stream()
                .filter(tag -> tag.getId().equals(id))
                .findAny()
                .ifPresent(tag -> {
                    throw new DuplicateException(className, "tag", tag.getName());
                });
        question.getTags().add(tagValidator.validateExists(id));
        return question;
    }

}
