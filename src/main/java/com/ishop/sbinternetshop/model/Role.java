package com.ishop.sbinternetshop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Integer roleId;
    @Enumerated(EnumType.STRING) // maps enum values as strings (default map type is Integer)
    @Column(length = 20, name = "role_name")
    @ToString.Include
    private AppRole roleName;

    public Role(AppRole roleName) {
        this.roleName = roleName;
    }
}
