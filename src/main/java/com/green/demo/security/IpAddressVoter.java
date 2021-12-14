package com.green.demo.security;

import com.green.demo.error.UnauthorizedException;
import com.green.demo.service.resource.SecurityResourceService;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Collection;
import java.util.List;

public class IpAddressVoter implements AccessDecisionVoter<Object> {

    private final SecurityResourceService securityResourceService;

    public IpAddressVoter(SecurityResourceService securityResourceService) {
        this.securityResourceService = securityResourceService;
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object o, Collection<ConfigAttribute> collection) {

        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String remoteAddress = details.getRemoteAddress();

        List<String> accessIpAddressList = securityResourceService.getAccessIpAddressList();

        int result = ACCESS_DENIED;

        for (String ipAddress : accessIpAddressList) {
            if (remoteAddress.equals(ipAddress)) {
                return ACCESS_ABSTAIN;
            }
        }

        if (result == ACCESS_DENIED) {
            throw new UnauthorizedException("Invalid Ip Address");
        }

        return 0;
    }
}
