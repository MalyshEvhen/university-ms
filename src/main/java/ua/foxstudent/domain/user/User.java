package ua.foxstudent.domain.user;


import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(
    name = "Users",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_users_email",
        columnNames = "email"),
    indexes = @Index(
        name = "idx_users_email",
        columnList = "email"))
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Email
    @NaturalId
    @Column(nullable = false, unique = true, updatable = false)
    private String email;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotEmpty
    @Setter(AccessLevel.PRIVATE)
    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "user_roles")
    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @UpdateTimestamp
    private LocalDateTime updateAt;

    @CreationTimestamp
    private LocalDateTime createAt;

    @Valid
    public User(@NotNull @Email String email,
                @NotNull @NotBlank String password) {
        this.email = email;
        this.password = password;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id == null || ((User) o).id == null) return false;
        if (!Objects.equals(id, user.id)) return false;
        if (!email.equals(user.email)) return false;
        return password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password);
    }
}
