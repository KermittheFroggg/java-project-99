package hexlet.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hexlet.code.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}