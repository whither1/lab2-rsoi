package org.lab2.bonus.repository;

import org.lab2.bonus.entity.PrivilegeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivilegeRepository extends JpaRepository<PrivilegeEntity, Integer> {

    Optional<PrivilegeEntity> findByUsername(String username);
}
