package com.ippon.employee.security.oauth2;

import com.ippon.employee.domain.Authority;
import com.ippon.employee.security.SecurityUtils;
import com.ippon.employee.service.gatewayclient.ApiKey;
import com.ippon.employee.service.gatewayclient.GatewayFeignClient;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtGrantedAuthorityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final GatewayFeignClient gatewayFeignClient;

    public JwtGrantedAuthorityConverter(GatewayFeignClient gatewayFeignClient) {
        // Bean extracting authority.
        this.gatewayFeignClient = gatewayFeignClient;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        List<GrantedAuthority> grantedAuthorities = SecurityUtils.extractAuthorityFromClaims(jwt.getClaims());
        if (grantedAuthorities == null || grantedAuthorities.isEmpty()) {

            try {
                ApiKey rolesForClientId = gatewayFeignClient.
                    getRolesForClientId("Bearer " + jwt.getTokenValue(), SecurityUtils.extractClientIdFromClaims(jwt.getClaims()));
                grantedAuthorities = rolesForClientId.getAuthorities()
                    .stream()
                    .map(Authority::getName)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            } catch (Exception e) {
                return grantedAuthorities;
            }

        }
        return grantedAuthorities;
    }
}
