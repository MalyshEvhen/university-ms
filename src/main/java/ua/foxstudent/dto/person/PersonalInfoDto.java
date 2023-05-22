package ua.foxstudent.dto.person;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonalInfoDto {
    @NotNull @Positive
    private Long id;
    @NotNull @NotBlank
    private String firstName;
    @NotNull @NotBlank
    private String lastName;
    @NotNull @NotBlank
    private String phone;
    @NotNull @NotBlank
    private String addressCity;
    @NotNull @NotBlank
    private String addressStreet;
    @NotNull @NotBlank
    private String addressNumber;
    private String addressApt;
    @NotNull @NotBlank
    private String addressZip;
    
    public PersonalInfoDto(
        Long id,
        String firstName,
        String lastName,
        String phone,
        String addressCity,
        String addressStreet,
        String addressNumber,
        String addressApt,
        String addressZip
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.addressCity = addressCity;
        this.addressStreet = addressStreet;
        this.addressNumber = addressNumber;
        this.addressApt = addressApt;
        this.addressZip = addressZip;
    }
}
