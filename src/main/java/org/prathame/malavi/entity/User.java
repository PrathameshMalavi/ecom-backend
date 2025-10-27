package org.prathame.malavi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "first_name")
    private String userFirstName;

    @Column(name = "last_name")
    private String userLastName;

    @Column(unique = true)
    public String keycloakId;

    @Column
    public String role;





//    @Column(unique = true)
//    public String username;

//    @Column
//    public String email;



//    @Column(name = "password")
//    private String userPassword;
//


//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "user_role",
//            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_name"), // Changed
////            joinColumns = @JoinColumn(name = "user_name", referencedColumnName = "userName"),
//            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "roleName")
//    )
//    private Set<Role> role;

}