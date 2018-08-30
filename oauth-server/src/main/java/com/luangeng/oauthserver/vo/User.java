package com.luangeng.oauthserver.vo;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String password;

    private String code;

    private String email;

    private String phone;

    private Boolean disable;

    private Boolean expire;

    private Boolean lock;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Role> role;

}
