package com.example.textilproject.repository;

import com.example.textilproject.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<Admin,Long> {

    @Query(value = "Select u from Admin u where u.email= :email ")
    Optional<Admin> fetchUserWithEmail(@Param("email") String email) ;

    @Query(value = "Select COUNT(u)>0 from Admin u where u.email= :email")
    boolean isEmailRegistered(@Param("email") String email);
}
