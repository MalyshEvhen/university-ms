package ua.foxstudent.domain.person;

import java.util.Objects;

import org.hibernate.Hibernate;
import org.hibernate.annotations.NaturalId;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ua.foxstudent.domain.user.Role;
import ua.foxstudent.domain.user.User;

@Getter
@Setter
@MappedSuperclass
public abstract class Person {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String firstName;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @NotNull
    @Embedded
    private Address address;

    @NotNull
    @NotBlank
    @NaturalId
    @Column(nullable = false, unique = true, updatable = false)
    private String phone;

    public void addRole(Role role) {
        this.user.addRole(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Person person = (Person) o;
        return getPhone() != null && Objects.equals(getPhone(), person.getPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone);
    }
}
