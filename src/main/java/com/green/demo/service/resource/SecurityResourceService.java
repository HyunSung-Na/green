package com.green.demo.service.resource;

import com.green.demo.model.user.Resources;
import com.green.demo.model.user.Role;
import com.green.demo.repository.resources.ResourcesRepository;
import com.green.demo.repository.user.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
public class SecurityResourceService {

    public static String RESOURCE_TYPE = "url";

    private final ResourcesRepository resourcesRepository;

    @Autowired
    private RoleRepository roleRepository;

    public SecurityResourceService(ResourcesRepository resourcesRepository) {
        this.resourcesRepository = resourcesRepository;
    }

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> hashMap = new LinkedHashMap<>();
        List<Resources> resources = resourcesRepository.findAll();

        resources.forEach(resource -> {
            if (Objects.equals(resource.getResourceType(), RESOURCE_TYPE)) {
                List<ConfigAttribute> configAttributes = resource.getRoleSet().stream()
                        .map(role -> new SecurityConfig(role.getRoleName())).collect(Collectors.toList());
                hashMap.put(new AntPathRequestMatcher(resource.getResourceName()), configAttributes);
            }
        });

        return hashMap;
    }

    // DB 초기화로 인한 인증 테스트를 위한 Resources 와 Role 세팅
    @PostConstruct
    public void setRoleAndResource() {
        Set<Role> roles = new HashSet<>();

        Role userRole = setRoleBuilder("ROLE_USER");

        userRole = roleRepository.save(userRole);

        Role adminRole = setRoleBuilder("ROLE_ADMIN");
        roleRepository.save(adminRole);

        Resources login = setResourcesBuilder("/api/auth", "url", "GET", 1, new HashSet<>());
        resourcesRepository.save(login);

        Resources join = setResourcesBuilder("/api/join", "url", "POST", 2, new HashSet<>());
        resourcesRepository.save(join);

        Resources exist = setResourcesBuilder("/api/user/exists", "url", "GET", 3, new HashSet<>());
        resourcesRepository.save(exist);

        roles.add(userRole);
        Resources user = setResourcesBuilder("/api/user/**", "url", "*", 4, roles);
        resourcesRepository.save(user);

        Resources all = setResourcesBuilder("/api/**", "url", "*", 6, roles);
        resourcesRepository.save(all);
    }

    private Role setRoleBuilder(String roleName) {
        return Role.builder()
                .roleName(roleName)
                .build();
    }

    private Resources setResourcesBuilder(String name, String type, String method, int orderNum, Set<Role> roles) {
        return Resources.builder()
                .resourceName(name)
                .resourceType(type)
                .httpMethod(method)
                .orderNum(orderNum)
                .roleSet(roles)
                .build();
    }
}
