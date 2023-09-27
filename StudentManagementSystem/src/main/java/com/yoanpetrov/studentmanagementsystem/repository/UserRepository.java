package com.yoanpetrov.studentmanagementsystem.repository;

import com.yoanpetrov.studentmanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUsersByCoursesCourseId(Long id);
}
