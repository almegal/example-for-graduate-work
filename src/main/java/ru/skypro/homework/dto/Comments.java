package ru.skypro.homework.dto;

import java.util.List;
import lombok.Data;

@Data
public class Comments {
    private Integer count;
    private List<Comment> results;
}
