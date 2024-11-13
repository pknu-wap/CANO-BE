package com.wap.cano_be.domain;

import com.wap.cano_be.domain.enums.Degree;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
    private Integer likeCount;

    @ElementCollection
    @CollectionTable(name = "menu_aromas", joinColumns = @JoinColumn(name = "menu_id"))
    @Column(name = "aromas")
    private List<String> aromas = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "like", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    public Menu() {}

    @Builder
    public Menu(String name, int price, double score, String imageUrl, double acidity, double body, double sweetness, double bitterness, List<String> aromas) {
        this.name = name;
        this.price = price;
        this.score = score;
        this.imageUrl = imageUrl;
        this.acidity = acidity;
        this.body = body;
        this.bitterness = bitterness;
        this.sweetness = sweetness;
        this.aromas = new ArrayList<>(aromas);
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }
}
