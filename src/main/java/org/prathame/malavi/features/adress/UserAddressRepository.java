package org.prathame.malavi.features.adress;

import jakarta.persistence.*;

import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class UserAddressRepository implements PanacheRepository<UserAddress> {

    public UserAddress findByUserName(String userName) {
        return find("userName", userName).firstResult();
    }
}
