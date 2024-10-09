package io.bemyplant.api.entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "userinfo")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "phones", length = 50)
    private String phones;

    @Column(name = "r_name", length = 50)
    private String r_name;

    @Column(name = "cre_date", length = 50)
    private String cre_date;

    @Column(name = "activated")
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
}