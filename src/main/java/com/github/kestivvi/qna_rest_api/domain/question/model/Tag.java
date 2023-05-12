package com.github.kestivvi.qna_rest_api.domain.question.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@Table(name = "TAGS")
public class Tag {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", initialValue = 101)
    private Long id;

    @NotBlank(message = "Field must not be blank")
    @Size(min = 1, max = 25, message = "Field must be between {min} and {max} characters")
    private String name;
}
