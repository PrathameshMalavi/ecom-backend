package org.prathame.malavi.features.carousel;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.prathame.malavi.entity.Product;

@ApplicationScoped
public class CarouselRepository implements PanacheRepository<Carousel> {

    @Transactional
    public void updateCarouse(Long id, Carousel updatedCarousel) {
        getEntityManager().merge(updatedCarousel);
    }

}
