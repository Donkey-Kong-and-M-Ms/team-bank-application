package com.cognixia.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.application.model.User;

@Repository("userRepo")
public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUserId(int userId);

	@Query(value = "select * from dbuser order by user_id desc limit 0,1", nativeQuery = true)
	public User findLargestUserId();
}
