package com.ippon.employee.service.gatewayclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "GATEWAY")
public interface GatewayFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/api-keys/roles/{clientId}")
    ApiKey getRolesForClientId(@RequestHeader("Authorization") String auth,  @PathVariable(name = "clientId") String clientId);
}
