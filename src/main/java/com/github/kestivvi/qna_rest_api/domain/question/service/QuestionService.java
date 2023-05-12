package com.github.kestivvi.qna_rest_api.domain.question.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.kestivvi.qna_rest_api.domain.question.dto.AnswerNewDto;
import com.github.kestivvi.qna_rest_api.domain.question.dto.AnswerShortDto;
import com.github.kestivvi.qna_rest_api.domain.question.dto.QuestionDto;
import com.github.kestivvi.qna_rest_api.domain.question.dto.TagDto;
import com.github.kestivvi.qna_rest_api.domain.question.model.Answer;
import com.github.kestivvi.qna_rest_api.domain.question.model.Question;
import com.github.kestivvi.qna_rest_api.domain.question.model.Tag;
import com.github.kestivvi.qna_rest_api.domain.question.repo.QuestionRepository;
import com.github.kestivvi.qna_rest_api.domain.user.AppUser;
import com.github.kestivvi.qna_rest_api.domain.user.AppUserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AppUserRepository appUserRepository;
    private final AnswerService answerService;
    private final TagService tagService;
    private final ModelMapper modelMapper;
    private final QuestionValidator validator;

    public QuestionDto convertToDto(Question p) {
        return modelMapper.map(p, QuestionDto.class);
    }

    public <T> Question convertToEntity(T dto) {
        return modelMapper.map(dto, Question.class);
    }

    public List<QuestionDto> getAllQuestions() {
        return questionRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<QuestionDto> getAllQuestions(Optional<List<String>> tags) {
        List<Question> questions = questionRepository.findAll();
        if (tags.isPresent()) {
            List<Tag> tagsEntities = tagService.getTagsByName(tags.get());
            if (!tagsEntities.isEmpty()) {
                for (Tag tag : tagsEntities) {
                    questions = questions.stream()
                            .filter(question -> question.getTags()
                                    .stream()
                                    .map(Tag::getId)
                                    .anyMatch(id -> id.equals(tag.getId())))
                            .toList();
                }
            }
        }
        return questions.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuestionDto getQuestionById(Long id) {
        return convertToDto(validator.validateExists(id));
    }


    @Transactional
    public QuestionDto addQuestion(QuestionDto question) {
        Question questionEntity = convertToEntity(question);
        questionEntity.setId(null);
        validateQuestion(questionEntity);
        return convertToDto(questionRepository.save(questionEntity));
    }

    @Transactional
    public QuestionDto updateQuestion(Long id, QuestionDto question) {
        validator.validateExists(id);
        Question questionEntity = convertToEntity(question);
        questionEntity.setId(id);
        validateQuestion(questionEntity);
        return convertToDto(questionRepository.save(questionEntity));
    }

    public void validateQuestion(Question question) {
        validator.validateNotDuplicate(question);
        validator.validateInput(question);
        question.getTags().forEach(tag -> validator.validateTagExists(tag.getId()));
        question.getAnswers().forEach(answer -> validator.validateAnswerExists(answer.getId()));
    }

    @Transactional
    public void deleteQuestion(Long id) {
        validator.validateExists(id);
        questionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AnswerShortDto> getAnswersOfQuestionById(Long id) {
        return validator.validateExists(id)
                .getAnswers()
                .stream()
                .map(answerService::convertShortToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TagDto> getTagsOfQuestionById(Long id) {
        return validator.validateExists(id)
                .getTags()
                .stream()
                .map(tagService::convertToDto)
                .toList();
    }

    @Transactional
    public AnswerShortDto addAnswerToQuestion(Long id, AnswerNewDto answerDto, String author_email) {
        Answer newAnswer = answerService.convertToEntity(answerDto);
        newAnswer.setQuestion(questionRepository.findById(id).get());
        
        AppUser author = appUserRepository.findByEmail(author_email).get();
        newAnswer.setAuthor(author);

        Question question = validator.validateExists(id);
        question.getAnswers().add(newAnswer);
        questionRepository.save(question);

        return answerService.convertShortToDto(
            question.getAnswers().get(question.getAnswers().size()-1)
        );
    }

    @Transactional
    public TagDto addTagToQuestion(Long id, TagDto tag) {
        Tag newTag = validator.validateTagExists(tag.getId());
        Question question = validator.validateExists(id);
        validator.validateQuestionNotContainsTag(question, newTag.getId());
        question.getTags().add(newTag);
        questionRepository.save(question);
        return tag;
    }

    @Transactional
    public void deleteAnswerFromQuestion(Long id, Long answerId) {
        Question question = validator.validateExists(id);
        Answer answer = validator.validateAnswerExists(answerId);
        validator.validateQuestionContainsAnswer(question, answer);
        question.getAnswers().remove(answer);
        questionRepository.save(question);
    }

    @Transactional
    public void deleteTagFromQuestion(Long id, Long tagId) {
        Question question = validator.validateExists(id);
        Tag tag = validator.validateTagExists(tagId);
        validator.validateQuestionContainsTag(question, tag);
        question.getTags().remove(tag);
        questionRepository.save(question);
    }
}
