package com.github.kestivvi.qna_rest_api.domain.question.model;


import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.github.kestivvi.qna_rest_api.domain.user.AppUser;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@Table(name = "QUESTIONS")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", initialValue = 101)
    private Long id;

    @NotBlank(message = "Field must not be blank")
    @Size(min = 1, max = 100, message = "Field must be between {min} and {max} characters")
    @Column
    private String title;

    @NotBlank(message = "Field must not be blank")
    @Size(min = 1, max = 10000, message = "Field must be between {min} and {max} characters")
    @Column
    private String description;

    @ManyToMany
    @JoinTable(name = "QUESTIONS_X_TAGS",
            joinColumns = @JoinColumn(name = "QUESTION_ID"),
            inverseJoinColumns = @JoinColumn(name = "TAG_ID"))
    private List<Tag> tags;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private AppUser author;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    private List<Answer> answers;

}
