package org.prathame.malavi.Common;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;


public class AuditListener {

    @Inject
    SecurityIdentity identity;

    @PrePersist
    public void prePersist(BaseEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setCreatedBy(getUsername());
        entity.setUpdatedBy(getUsername());
    }

    @PreUpdate
    public void preUpdate(BaseEntity entity) {
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(getUsername());
    }

    public String getUsername(){
        try{
            String username = (identity != null && identity.isAnonymous() == false)
                    ? identity.getPrincipal().getName()
                    : "SYSTEM";
        }catch (Exception e){
//            return "SYSTEM";
            System.out.println("Audit Listener : " + e.toString());
        }
        return "SYSTEM";
    }
}