package com.cognixia.application.repository;

import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import com.cognixia.application.model.User;


@Repository("userRepo")
public interface UserRepository extends JpaRepository<User, Integer> {

}
