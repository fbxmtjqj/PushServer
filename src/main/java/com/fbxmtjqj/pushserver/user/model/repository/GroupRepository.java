package com.fbxmtjqj.pushserver.user.model.repository;

import com.fbxmtjqj.pushserver.user.model.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(final String name);
}
