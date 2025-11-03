package org.example.authenticationservice.controller;

import org.example.authenticationservice.entity.Roles;
import org.example.authenticationservice.security.JwtTokenService;
import org.example.authenticationservice.service.PolicyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class ForwardAuthController {

    private final JwtTokenService jwt;
    private final PolicyService policy;

    @RequestMapping(path = "/validate", method = { RequestMethod.GET, RequestMethod.POST,
            RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.OPTIONS })
    public void forwardAuth(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpStatus.OK.value());
            return;
        }

        String token = req.getHeader("app-auth");
        if (!StringUtils.hasText(token)) {
            res.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing token");
            return;
        }

        var auth = jwt.getAuthenticationFromToken(token);

        Set<Roles> roles = auth.getAuthorities().stream()
                .map(a -> Roles.valueOf(a.getAuthority().replace("ROLE_", "")))
                .collect(java.util.stream.Collectors.toSet());

        String method = headerOrDefault(req, "X-Forwarded-Method", req.getMethod());
        String uri    = headerOrDefault(req, "X-Forwarded-Uri",    req.getRequestURI());

        boolean allowed = policy.isAllowed(method, uri, roles);
        if (!allowed) {
            res.sendError(HttpStatus.FORBIDDEN.value(), "Forbidden by policy");
            return;
        }

        res.setStatus(HttpStatus.OK.value());
    }

    private static String headerOrDefault(HttpServletRequest req, String name, String fallback) {
        String v = req.getHeader(name);
        return StringUtils.hasText(v) ? v : fallback;
    }
}