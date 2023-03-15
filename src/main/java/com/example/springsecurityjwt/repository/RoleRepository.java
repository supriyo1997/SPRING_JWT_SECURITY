package com.example.springsecurityjwt.repository;


import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springsecurityjwt.model.Role;
import com.example.springsecurityjwt.model.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	final static Logger logger = LoggerFactory.getLogger(RoleRepository.class);
	
	 Optional<Role> findByName(RoleName roleName);
}
