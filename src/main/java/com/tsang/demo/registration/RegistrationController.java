package com.tsang.demo.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/registration")
//@RequestMapping(path = "registration")
@AllArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;
    @PostMapping
    @ExceptionHandler
    public String register(@RequestBody RegistrationRequest registrationRequest){
        return registrationService.register(registrationRequest);
    }
    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token){
        return  registrationService.confirmToken(token);
    }
}
