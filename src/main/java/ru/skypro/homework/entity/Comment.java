package ru.skypro.homework.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authorImage;
    @NotBlank
    private String authorFirstName;
    @NotNull
    private Long createdAt;
    @NotBlank
    private String text;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Long author;
}
