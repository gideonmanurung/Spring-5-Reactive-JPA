package com.reactive.spring.accounts.repository;

import com.reactive.spring.accounts.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Integer>{
}
