package br.edu.ifpb.producer.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
@Getter @Setter @EqualsAndHashCode(of = "id") @ToString(of = "id")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PermissionEntity> permissions = new ArrayList<>();

    public void addPermission(PermissionEntity permissionEntity) {
        this.permissions.add(permissionEntity);
        permissionEntity.setUser(this);
    }
}
