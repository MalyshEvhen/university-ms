package ua.foxstudent.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxstudent.domain.person.Address;
import ua.foxstudent.domain.person.Student;
import ua.foxstudent.dto.person.AddressDto;
import ua.foxstudent.dto.person.PersonalInfoDto;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("""
        select new ua.foxstudent.dto.person.PersonalInfoDto(
            s.id,
            s.firstName,
            s.lastName,
            s.phone,
            s.address.city,
            s.address.street,
            s.address.number,
            s.address.apt,
            s.address.zip
        )
        from Student s
        where s.id = ?1""")
    Optional<PersonalInfoDto> findPersonalInfoDtoById(@NotNull Long id);

    @Query("""
        select new ua.foxstudent.dto.person.AddressDto(
            s.id,
            s.address.city,
            s.address.street,
            s.address.number,
            s.address.apt,
            s.address.zip
        )
        from Student s
        where s.id = ?1""")
    Optional<AddressDto> findAddressDtoById(Long id);

    @Modifying
    @Query("update Student s set s.address = ?1 where s.id = ?2")
    void updateAddressById(Address address, Long id);
}
