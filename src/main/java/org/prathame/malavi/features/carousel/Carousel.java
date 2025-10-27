package org.prathame.malavi.features.carousel;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@Table(name = "carousel")
public class Carousel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "url")
    private String url;

    @Column(name = "description", length = 500)
    private String description;

    // Constructors
    public Carousel() {
    }

    public Carousel(String imageUrl, String url, String description) {
        this.imageUrl = imageUrl;
        this.url = url;
        this.description = description;
    }

    public Carousel(Long id , String imageUrl, String url, String description) {
        this.imageUrl = imageUrl;
        this.url = url;
        this.description = description;
    }
}