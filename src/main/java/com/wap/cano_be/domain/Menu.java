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
    private long id;
    private String name;
    private int price;
    private double score;
    private String imageUrl;
    private double acidity;
    private double body;
    private double bitterness;
    private double sweetness;

    @ElementCollection
    @CollectionTable(name = "menu_aromas", joinColumns = @JoinColumn(name = "menu_id"))
    @Column(name = "aroma")
    private List<String> aromas = new ArrayList<>();

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
}
