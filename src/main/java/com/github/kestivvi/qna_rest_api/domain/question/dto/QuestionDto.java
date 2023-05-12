package com.github.kestivvi.qna_rest_api.domain.question.dto;

import java.util.List;

import com.github.kestivvi.qna_rest_api.domain.question.model.Tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {

    private Long id;
    private String title;
    private String description;
    private List<Tag> tags;

}
