package ru.skypro.homework.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ads")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String description;

    @NotNull
    private Integer price;

    @NotBlank
    private String title;

    private String imageUrl;
    // Путь к файлу на диске, состоящий только из имени файла (без имени папки и /)

    // Если будет LazyException, то переделать на графы
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
    // cascade = CascadeType.ALL - наличие данного выражения обеспечит удаление всех комментариев при удалении
    // объявления
    // orphanRemoval = true - если этот параметр выставлен в true, то дочерняя сущность будет удалена, если на нее
    // исчезли все ссылки. Если несколько родительских сущностей ссылаются на одну дочернюю, то выгодно, чтобы она
    // удалялась не вместе с удалением родительской сущности, а только если все ссылки на нее будут обнулены.
}
