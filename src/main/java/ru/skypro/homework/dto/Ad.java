package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class Ad {
    private Integer id;
    private String image;
    private Integer pk;
    private Integer price;
    private String title;
}
