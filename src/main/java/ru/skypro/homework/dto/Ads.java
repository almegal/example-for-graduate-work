package ru.skypro.homework.dto;

import java.util.List;
import lombok.Data;

@Data
public class Ads {
    private Integer count;
    private List<Ad> results;
}
