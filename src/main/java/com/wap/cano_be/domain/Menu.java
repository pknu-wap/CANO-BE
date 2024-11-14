package com.wap.cano_be.domain;

import com.wap.cano_be.domain.enums.Degree;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
public class Menu {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer price;
    private Double score;
    private String imageUrl;
    private Double acidity;
    private Double body;
    private Double bitterness;
    private Double sweetness;
    private Integer likeCount = 0;
    private Integer reviewCount = 0;

    @ElementCollection
    @CollectionTable(name = "menu_aromas", joinColumns = @JoinColumn(name = "menu_id"))
    @MapKeyColumn(name = "aroma")
    @Column(name = "count")
    private Map<String, Integer> aromas = new HashMap<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "like", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    public Menu() {}

    @Builder
    public Menu(String name, int price, double score, String imageUrl, double acidity, double body, double sweetness, double bitterness, List<String> aromas, int likeCount, int reviewCount, List<Review> reviews,List<Like> likes) {
        this.name = name;
        this.price = price;
        this.score = score;
        this.imageUrl = imageUrl;
        this.acidity = acidity;
        this.body = body;
        this.bitterness = bitterness;
        this.sweetness = sweetness;
        this.likeCount = likeCount;
        this.reviewCount = reviewCount;
        this.reviews = reviews;
        this.likes = likes;
        for (String aroma: aromas) {
            this.aromas.put(aroma.toLowerCase(), 1);
        }
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public void increaseReviewCount() {
        this.reviewCount++;
    }

    public void decreaseReviewCount() {
        this.reviewCount--;
    }

    public void calculateByReview(double score, double acidity, double body, double bitterness, double sweetness, List<String> aromas){
        if(reviewCount == 0){
            this.score = score;
            this.acidity = acidity;
            this.body = body;
            this.bitterness = bitterness;
            this.sweetness = sweetness;
            reviewCount++;
        } else {
            reviewCount++;
            this.score = (this.score + score) / reviewCount;
            this.acidity = (this.acidity + acidity) / reviewCount;
            this.body = (this.body + body) / reviewCount;
            this.bitterness = (this.bitterness + bitterness) / reviewCount;
            this.sweetness = (this.sweetness + sweetness) / reviewCount;
        }

        for (String aroma: aromas) {
            this.aromas.put(aroma.toLowerCase(), this.aromas.getOrDefault(aroma, 0) + 1);
        }
    }
}
