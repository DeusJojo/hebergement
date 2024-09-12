package com.afpa.hebergement.security.service;

import com.afpa.hebergement.model.entity.AppUser;
import com.afpa.hebergement.model.repository.AppUserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    AppUserRepository appUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String number) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByBeneficiaryNumber(number).orElseThrow(() -> new UsernameNotFoundException(number));
        return UserDetailsImpl.build(user);
    }

}
