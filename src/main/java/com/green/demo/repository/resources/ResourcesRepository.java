package com.green.demo.repository.resources;

import com.green.demo.model.user.Resources;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {
}
