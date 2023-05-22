package ua.foxstudent.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxstudent.domain.person.AcademicDegree;
import ua.foxstudent.domain.person.Address;
import ua.foxstudent.domain.person.ProfessionalPost;
import ua.foxstudent.domain.person.Teacher;
import ua.foxstudent.dto.person.AddressDto;
import ua.foxstudent.dto.person.PersonalInfoDto;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query("""
        select new ua.foxstudent.dto.person.PersonalInfoDto(
            t.id,
            t.firstName,
            t.lastName,
            t.phone,
            t.address.city,
            t.address.street,
            t.address.number,
            t.address.apt,
            t.address.zip
        )
        from Teacher t
        where t.id = ?1""")
    Optional<PersonalInfoDto> findPersonalInfoById(Long id);
    @Query("""
        select new ua.foxstudent.dto.person.AddressDto(
            t.id,
            t.address.city,
            t.address.street,
            t.address.number,
            t.address.apt,
            t.address.zip
        )
        from Teacher t
        where t.id = ?1""")
    Optional<AddressDto> findAddressDtoById(Long id);

    @Modifying
    @Query("update Teacher t set t.professionalPost = ?1 where t.id = ?2")
    void updateProfessionalPostById(ProfessionalPost professionalPost, Long id);

    @Modifying
    @Query("update Teacher t set t.academicDegree = ?1 where t.id = ?2")
    void updateAcademicDegreeById(AcademicDegree academicDegree, Long id);

    @Modifying
    @Query("update Teacher t set t.address = ?1 where t.id = ?2")
    void updateAddressById(Address address, Long id);

}
