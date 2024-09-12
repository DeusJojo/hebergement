package com.afpa.hebergement.security.jwt;

import com.afpa.hebergement.model.repository.AppUserRepository;
import com.afpa.hebergement.security.service.UserDetailsImpl;
import com.afpa.hebergement.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AppUserRepository repo;

    private static final Logger Authlogger = LoggerFactory
            .getLogger(AuthTokenFilter.class);


    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if(jwt != null && jwtUtils.validateJwtToken(jwt)){
                String number = jwtUtils.getBenefNumberFromToken(jwt);
                UserDetails userDetails = UserDetailsImpl.build(repo.findByBeneficiaryNumber(number).orElseThrow(() -> new RuntimeException("User not found")));
                if (userDetails == null) {
                    Authlogger.info("User {}", "Not found");
                    return;
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e){
            Authlogger.error("Cannot set user authentication{}" ,e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {

            return headerAuth.substring(7);
        }
        return null;
    }
}
