package com.github.kestivvi.qna_rest_api.domain.question.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.kestivvi.qna_rest_api.domain.question.dto.TagDto;
import com.github.kestivvi.qna_rest_api.domain.question.model.Tag;
import com.github.kestivvi.qna_rest_api.domain.question.repo.TagRepository;
import com.github.kestivvi.qna_rest_api.validation.exceptions.NotFoundException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final TagValidator validator;

    public TagDto convertToDto(Tag t) {
        return modelMapper.map(t, TagDto.class);
    }

    public <T> Tag convertToEntity(T dto) {
        return modelMapper.map(dto, Tag.class);
    }

    @Transactional(readOnly = true)
    public List<TagDto> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TagDto getTagById(Long id) {
        return convertToDto(validator.validateExists(id));
    }

    @Transactional
    public TagDto addTag(TagDto tagDto) {
        Tag tag = convertToEntity(tagDto);
        tag.setId(null);
        validator.validateInput(tag);
        validator.validateNotDuplicate(tag);
        return convertToDto(tagRepository.save(tag));
    }

    @Transactional
    public void updateTag(Long id, TagDto tagDto) {
        Tag tag = convertToEntity(tagDto);
        tag.setId(id);
        validator.validateExists(id);
        validator.validateInput(tag);
        validator.validateNotDuplicate(tag);
        tagRepository.save(tag);
    }

    @Transactional
    public void deleteTag(Long id) {
        validator.validateExists(id);
        tagRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Tag> getTagsByName(List<String> tagsList) {
        List<Tag> result = new ArrayList<>();
        List<Tag> allTags = tagRepository.findAll();
        for (String tagName : tagsList) {
            if (allTags.stream().anyMatch(t -> t.getName().equalsIgnoreCase(tagName))) {
                result.add(allTags.stream().filter(g -> g.getName().equalsIgnoreCase(tagName)).findFirst().get());
            } else {
                throw new NotFoundException("Tag", tagName);
            }
        }
        return result;
    }
}
