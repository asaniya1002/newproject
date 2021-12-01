package com.hcl.repositories;

import com.hcl.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
    User getById(Long id);

    //findById would return an Optional<User> and we just dont want it in this project...personal preference
    //so we will be using getById
}