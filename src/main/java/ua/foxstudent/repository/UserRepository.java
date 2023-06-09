package ua.foxstudent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxstudent.domain.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
