package com.example.EmployeeManagement.repository;
import com.example.EmployeeManagement.dto.*;
import com.example.EmployeeManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String userName);
    boolean existsByEmail(String email);
    User findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<UserProjection> findBy();

}