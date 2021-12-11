package com.green.demo.service.user;

import com.green.demo.model.user.RoleHierarchy;
import com.green.demo.repository.user.RoleHierarchyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleHierarchyService {

    private final RoleHierarchyRepository roleHierarchyRepository;
    private final RoleHierarchyImpl roleHierarchy;

    public String findAllHierarchy() {
        List<RoleHierarchy> roleHierarchy = roleHierarchyRepository.findAll();
        Iterator<RoleHierarchy> iterator = roleHierarchy.iterator();
        StringBuilder concatRoles = new StringBuilder();

        while (iterator.hasNext()) {
            RoleHierarchy next = iterator.next();
            if (next.getParentName() != null) {
                concatRoles.append(next.getParentName().getChildName());
                concatRoles.append(" > ");
                concatRoles.append(next.getChildName());
                concatRoles.append("\n");
            }
        }

        return concatRoles.toString();
    }

    @PostConstruct
    public void initHierarchy() {
        // test RoleHierarchy setting
        RoleHierarchy admin = RoleHierarchy.builder()
                .childName("ROLE_ADMIN")
                .build();
        admin = roleHierarchyRepository.save(admin);

        RoleHierarchy user = RoleHierarchy.builder()
                .childName("ROLE_USER")
                .parentName(admin)
                .build();

        roleHierarchyRepository.save(user);

        // init hierarchy setting
        String hierarchy = findAllHierarchy();
        this.roleHierarchy.setHierarchy(hierarchy);
    }
}
