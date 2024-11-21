package com.wap.cano_be.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.*;

@Entity
@Getter
public class Menu {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "menu", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    public Menu() {}

    @Builder
    public Menu(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Double getAverageScore() {
        OptionalDouble average = reviews.stream()
                .map(Review::getScore)
                .filter(Objects::nonNull) // null 값 제외
                .mapToDouble(Double::doubleValue)
                .average();

        return average.isPresent() ? average.getAsDouble() : null; // 평균값 반환
    }

    public Double getAverageAcidity() {
        OptionalDouble average = reviews.stream()
                .map(Review::getAcidity)
                .filter(Objects::nonNull) // null 값 제외
                .mapToDouble(Double::doubleValue)
                .average();

        return average.isPresent() ? average.getAsDouble() : null; // 평균값 반환
    }

    public Double getAverageBody() {
        OptionalDouble average = reviews.stream()
                .map(Review::getBody)
                .filter(Objects::nonNull) // null 값 제외
                .mapToDouble(Double::doubleValue)
                .average();

        return average.isPresent() ? average.getAsDouble() : null; // 평균값 반환
    }

    public Double getAverageBitterness() {
        OptionalDouble average = reviews.stream()
                .map(Review::getBitterness)
                .filter(Objects::nonNull) // null 값 제외
                .mapToDouble(Double::doubleValue)
                .average();

        return average.isPresent() ? average.getAsDouble() : null; // 평균값 반환
    }

    public Double getAverageSweetness() {
        OptionalDouble average = reviews.stream()
                .map(Review::getSweetness)
                .filter(Objects::nonNull) // null 값 제외
                .mapToDouble(Double::doubleValue)
                .average();

        return average.isPresent() ? average.getAsDouble() : null; // 평균값 반환
    }
}
