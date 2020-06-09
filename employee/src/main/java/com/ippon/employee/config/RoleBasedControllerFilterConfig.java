package com.ippon.employee.config;

import com.ippon.employee.domain.Employee;
import com.ippon.employee.domain.view.View;
import com.ippon.employee.security.AuthoritiesConstants;
import com.ippon.employee.web.rest.EmployeeResource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice(assignableTypes = EmployeeResource.class)
public class RoleBasedControllerFilterConfig extends AbstractMappingJacksonResponseBodyAdvice {

    private  Map<String, Class> viewMapping = new HashMap<>();

    public RoleBasedControllerFilterConfig() {
        viewMapping.put(AuthoritiesConstants.ADMIN, View.Admin.class);
    }

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
                                           MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
        if (SecurityContextHolder.getContext().getAuthentication() != null
            && SecurityContextHolder.getContext().getAuthentication().getAuthorities() != null) {
            Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            List<Class> jsonViews = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(viewMapping::containsKey)
                .map(viewMapping::get)
                .collect(Collectors.toList());
            if (jsonViews.size() == 1) {
                bodyContainer.setSerializationView(jsonViews.get(0));
            } else {
                bodyContainer.setSerializationView(View.User.class);
            }
        }
    }
}
