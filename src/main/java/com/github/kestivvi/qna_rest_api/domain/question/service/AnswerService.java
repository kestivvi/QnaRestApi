package com.github.kestivvi.qna_rest_api.domain.question.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

import com.github.kestivvi.qna_rest_api.domain.question.dto.AnswerShortDto;
import com.github.kestivvi.qna_rest_api.domain.question.model.Answer;
import com.github.kestivvi.qna_rest_api.domain.question.repo.AnswerRepository;

@AllArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final ModelMapper modelMapper;
    private final AnswerValidator validator;

    public AnswerShortDto convertShortToDto(Answer p) {
        return modelMapper.map(p, AnswerShortDto.class);
    }

    public <T> Answer convertToEntity(T dto) {
        return modelMapper.map(dto, Answer.class);
    }

    @Transactional(readOnly = true)
    public List<AnswerShortDto> getAllAnswers() {
        return answerRepository.findAll()
                .stream()
                .map(this::convertShortToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public AnswerShortDto getAnswerById(long id) {
        return convertShortToDto(validator.validateExists(id));
    }

    @Transactional
    public AnswerShortDto addAnswer(AnswerShortDto answerDto) {
        Answer answer = convertToEntity(answerDto);
        answer.setId(null);
        validator.validateInput(answer);
        validator.validateNotDuplicate(answer);
        return convertShortToDto(answerRepository.save(answer));
    }

    @Transactional
    public void updateAnswer(Long id, AnswerShortDto answerDto) {
        Answer answer = convertToEntity(answerDto);
        answer.setId(id);
        validator.validateExists(id);
        validator.validateInput(answer);
        validator.validateNotDuplicate(answer);
        answerRepository.save(answer);
    }

    @Transactional
    public void deleteAnswer(Long id) {
        validator.validateExists(id);
        answerRepository.deleteById(id);
    }

}
