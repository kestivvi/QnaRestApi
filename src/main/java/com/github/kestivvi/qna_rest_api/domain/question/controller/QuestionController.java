package com.github.kestivvi.qna_rest_api.domain.question.controller;


import java.net.URI;
import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import com.github.kestivvi.qna_rest_api.domain.question.dto.AnswerNewDto;
import com.github.kestivvi.qna_rest_api.domain.question.dto.AnswerShortDto;
import com.github.kestivvi.qna_rest_api.domain.question.dto.QuestionDto;
import com.github.kestivvi.qna_rest_api.domain.question.dto.TagDto;
import com.github.kestivvi.qna_rest_api.domain.question.service.QuestionService;

@AllArgsConstructor
@RestController
@RequestMapping("/${app.prefix}/${app.version}/questions")
@Tag(name = "Question", description = "Question endpoints")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping()
    @Operation(summary = "Get all questions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the questions",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = QuestionDto.class)))}),
            @ApiResponse(responseCode = "204", description = "Questions not found", content = @Content)
    })
    public ResponseEntity<List<QuestionDto>> getAllQuestions() {
        List<QuestionDto> questions = questionService.getAllQuestions();
        if (questions.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get question by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the question",
                    content = {@Content(schema = @Schema(implementation = QuestionDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request, wrong id",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Question not found", content = @Content)
    })
    public ResponseEntity<QuestionDto> getQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }

    @GetMapping("/{id}/answers")
    @Operation(summary = "Get answers of question by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the answers",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = AnswerShortDto.class)))}),
            @ApiResponse(responseCode = "204", description = "No answers found", content = @Content),
            @ApiResponse(responseCode = "404", description = "Question not found", content = @Content)
    })
    public ResponseEntity<List<AnswerShortDto>> getAnswersOfQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getAnswersOfQuestionById(id));
    }

    @GetMapping("/{id}/tags")
    @Operation(summary = "Get tags of question by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the tagss",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = TagDto.class)))}),
            @ApiResponse(responseCode = "204", description = "No tags found", content = @Content),
            @ApiResponse(responseCode = "404", description = "Question not found", content = @Content)
    })
    public ResponseEntity<List<TagDto>> getTagsOfQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getTagsOfQuestionById(id));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Add new question", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Question created",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request, wrong data",
                    content = @Content)
    })
    public ResponseEntity<Void> addQuestion(@RequestBody QuestionDto question) {
        QuestionDto questionDto = questionService.addQuestion(question);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(questionDto.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{id}/answers")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Add answer to question", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Answer added",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request, wrong data",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Question not found", content = @Content)
    })
    public ResponseEntity<Void> addAnswerToQuestion(@PathVariable Long id, @RequestBody AnswerNewDto answerDto, Principal principal) {
        String author_email = principal.getName();
        AnswerShortDto AnswerDto = questionService.addAnswerToQuestion(id, answerDto, author_email);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(AnswerDto.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{id}/tags")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Add tag to question", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tag added",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request, wrong data",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Question not found", content = @Content)
    })
    public ResponseEntity<Void> addTagToQuestion(@PathVariable Long id, @RequestBody TagDto tagDto) {
        TagDto TagDto = questionService.addTagToQuestion(id, tagDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(TagDto.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @Operation(summary = "Update question", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question updated",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request, wrong data",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Question not found", content = @Content)
    })
    public ResponseEntity<Void> updateQuestion(@PathVariable Long id, @RequestBody QuestionDto question) {
        questionService.updateQuestion(id, question);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @Operation(summary = "Delete question", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Question deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Question not found", content = @Content)
    })
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/answers/{answerId}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @Operation(summary = "Delete answer from question", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Answer deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Question or answer not found", content = @Content)
    })
    public ResponseEntity<Void> deleteAnswerFromQuestion(@PathVariable Long id, @PathVariable Long answerId) {
        questionService.deleteAnswerFromQuestion(id, answerId);
        return ResponseEntity.noContent().build();
    }

}
