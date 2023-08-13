package com.tsang.demo.appuser;

import com.tsang.demo.registration.RegistrationService;
import com.tsang.demo.registration.token.ConfirmationToken;
import com.tsang.demo.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    private final AppUserRepository appUserRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return appUserRepository.findByEmail(email).
                orElseThrow(()-> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(AppUser appUser){
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
        if(userExists){
            throw new IllegalArgumentException("email already taken");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);

        // Send confirmation token
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
            token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15),appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);




        // SEND EMAIL

        return confirmationToken.getToken();
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
