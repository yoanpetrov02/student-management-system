package com.yoanpetrov.studentmanagementsystem.repositories;

import com.yoanpetrov.studentmanagementsystem.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUsersByCoursesCourseId(Long id);
}
