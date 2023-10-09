package com.yoanpetrov.studentmanagementsystem.repository;

import com.yoanpetrov.studentmanagementsystem.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

}
