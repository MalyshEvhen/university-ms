package ua.foxstudent.domain.person;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String city;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String street;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String number;

    private String apt;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String zip;

    public Address(@NotNull @NotBlank String city,
                   @NotNull @NotBlank String street,
                   String number,
                   @NotNull @NotBlank String zip) {
        this.city = city;
        this.street = street;
        this.number = number;
        this.zip = zip;
    }
}
