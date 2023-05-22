package ua.foxstudent.dto.person;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDto {
    @NotNull @Positive
    private Long id;
    @NotNull @NotBlank
    private String city;
    @NotNull @NotBlank
    private String street;
    @NotNull @NotBlank
    private String number;
    private String apt;
    @NotNull @NotBlank
    private String zip;
    
    public AddressDto(
        Long id,
        String city,
        String street,
        String number,
        String apt,
        String zip
    ) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.number = number;
        this.apt = apt;
        this.zip = zip;
    }
}
