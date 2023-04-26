package com.baseurak.AFDay.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 * @Author: Ru
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
    List<User> findAll();
    //void save(User user);
}