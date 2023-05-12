package com.github.kestivvi.qna_rest_api.domain.question.service;


import org.springframework.stereotype.Service;

import com.github.kestivvi.qna_rest_api.domain.question.model.Tag;
import com.github.kestivvi.qna_rest_api.domain.question.repo.TagRepository;
import com.github.kestivvi.qna_rest_api.validation.CustomValidator;
import com.github.kestivvi.qna_rest_api.validation.ValidationService;
import com.github.kestivvi.qna_rest_api.validation.exceptions.DuplicateException;
import com.github.kestivvi.qna_rest_api.validation.exceptions.NotFoundException;

import jakarta.validation.Validator;

@Service
public class TagValidator extends CustomValidator<Tag> implements ValidationService<Tag> {

    private final TagRepository repository;

    public TagValidator(Validator validator, TagRepository repository) {
        super(validator);
        this.repository = repository;
    }

    @Override
    public Tag validateExists(long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(className, id));
    }

    @Override
    public void validateNotDuplicate(Tag tag) {
        repository.findByName(tag.getName()).ifPresent(t -> {
            throw new DuplicateException(className, "name", t.getName());
        });
    }

}
