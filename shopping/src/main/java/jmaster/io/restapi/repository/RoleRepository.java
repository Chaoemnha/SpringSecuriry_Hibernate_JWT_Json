package jmaster.io.restapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jmaster.io.restapi.model.Role;
public interface RoleRepository extends JpaRepository<Role, Integer>{
	Optional<Role> findByRolename(String rolename);
}
