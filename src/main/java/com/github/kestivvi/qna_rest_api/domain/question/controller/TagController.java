package com.github.kestivvi.qna_rest_api.domain.question.controller;

import java.net.URI;
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
import com.github.kestivvi.qna_rest_api.domain.question.dto.TagDto;
import com.github.kestivvi.qna_rest_api.domain.question.service.TagService;

@AllArgsConstructor
@RestController
@RequestMapping("/${app.prefix}/${app.version}/tags")
@Tag(name = "Tag", description = "Question Tag")
public class TagController {

    private final TagService tagService;

    @GetMapping()
    @Operation(summary = "Get all tags")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the tags",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = TagDto.class)))}),
            @ApiResponse(responseCode = "204", description = "Tags not found",
                    content = @Content)
    })
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<TagDto> tags = tagService.getAllTags();
        if (tags.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tag by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the tag",
                    content = {@Content(schema = @Schema(implementation = TagDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request, wrong id",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Tag not found", content = @Content)
    })
    public ResponseEntity<TagDto> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @Operation(summary = "Add new tag", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tag created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request, wrong answer", content = @Content)
    })
    public ResponseEntity<Void> addTag(@RequestBody TagDto tagDto) {
        TagDto tag = tagService.addTag(tagDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tag.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @Operation(summary = "Update tag", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag updated",
                    content = {@Content(schema = @Schema(implementation = TagDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Tag not found", content = @Content)
    })
    public ResponseEntity<Void> updateTag(@PathVariable Long id, @RequestBody TagDto tagDto) {
        tagService.updateTag(id, tagDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @Operation(summary = "Delete tag", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Tag not found", content = @Content)
    })
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

}

