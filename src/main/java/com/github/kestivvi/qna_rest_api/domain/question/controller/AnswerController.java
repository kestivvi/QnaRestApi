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

import com.github.kestivvi.qna_rest_api.domain.question.dto.AnswerShortDto;
import com.github.kestivvi.qna_rest_api.domain.question.service.AnswerService;

@AllArgsConstructor
@RestController
@RequestMapping("/${app.prefix}/${app.version}/answers")
@Tag(name = "Answer", description = "Question Answer")
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping()
    @Operation(summary = "Get all answers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the answers",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = AnswerShortDto.class)))}),
            @ApiResponse(responseCode = "204", description = "Answers not found",
                    content = @Content)
    })
    public ResponseEntity<List<AnswerShortDto>> getAllAnswers() {
        List<AnswerShortDto> answers = answerService.getAllAnswers();
        if (answers.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get answer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the answer",
                    content = {@Content(schema = @Schema(implementation = AnswerShortDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request, wrong id",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Answer not found", content = @Content)
    })
    public ResponseEntity<AnswerShortDto> getAnswerById(@PathVariable Long id) {
        return ResponseEntity.ok(answerService.getAnswerById(id));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Add new answer", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Answer created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request, wrong answer", content = @Content)
    })
    public ResponseEntity<Void> addAnswer(@RequestBody AnswerShortDto answerDto, Principal principal) {
        System.out.println(principal.getName());

        AnswerShortDto answer = answerService.addAnswer(answerDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(answer.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @Operation(summary = "Update answer", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Answer updated",
                    content = {@Content(schema = @Schema(implementation = AnswerShortDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Answer not found", content = @Content)
    })
    public ResponseEntity<Void> updateAnswer(@PathVariable Long id, @RequestBody AnswerShortDto answerDto) {
        answerService.updateAnswer(id, answerDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @Operation(summary = "Delete answer", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Answer deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Answer not found", content = @Content)
    })
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        answerService.deleteAnswer(id);
        return ResponseEntity.noContent().build();
    }

}

