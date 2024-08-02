package com.luv2code.CryptoTrading.controller;


import com.luv2code.CryptoTrading.Utils.OtpUtils;
import com.luv2code.CryptoTrading.config.JwtProvider;
import com.luv2code.CryptoTrading.model.TwoFactorOtp;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.repository.UserRepository;
import com.luv2code.CryptoTrading.request.LoginRequest;
import com.luv2code.CryptoTrading.response.AuthResponse;
import com.luv2code.CryptoTrading.service.CustomUserService;
import com.luv2code.CryptoTrading.service.EmailService;
import com.luv2code.CryptoTrading.service.TwoFactorOtpService;
import com.luv2code.CryptoTrading.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WatchListService watchListService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        User isUserExists = userRepository.findByEmail(user.getEmail());
        if (isUserExists != null) {
            throw new Exception("Email already exist with another Account!");
        }

        User createUser = new User();
        createUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createUser.setEmail(user.getEmail());
        createUser.setFullName(user.getFullName());
        createUser.setUsername(user.getUsername());

        User savedUser = userRepository.save(createUser);
        watchListService.createWatchList(savedUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = JwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("SignUp Successful!");
        authResponse.setJwt(jwt);
        authResponse.setStatus(true);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody User user) throws Exception {

        String email = user.getEmail();
        String password = user.getPassword();
        Authentication authentication = autheticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = JwtProvider.generateToken(authentication);

        User authUser = userRepository.findByEmail(email);
        if (user.getTwofactorAuth().isEnabled()) {
            AuthResponse response = new AuthResponse();
            response.setMessage("Two Factor is enabled");
            response.setTwoFactorEnabled(true);
            String otp = OtpUtils.generateOtp();
            TwoFactorOtp oldOtp = twoFactorOtpService.findByUser(authUser.getId());
            if (oldOtp != null) {
                twoFactorOtpService.deleteTwoFactorOtp(oldOtp);
            }

            TwoFactorOtp newOtp = twoFactorOtpService.createTwoFactorOtp(authUser, otp, jwt);
            emailService.sendEmaiVerificationWithOtp(email, newOtp.getOtp());
            response.setSession(newOtp.getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Signin Successful!");
        authResponse.setJwt(jwt);
        authResponse.setStatus(true);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);

    }

    private Authentication autheticate(String username, String password) {
        UserDetails userDetails = customUserService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid Username");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Password Provided!");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }


    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySignin(@PathVariable String otp, @RequestParam String id) throws Exception {
        TwoFactorOtp twoFactorOtp = twoFactorOtpService.findById(id);
        if (twoFactorOtpService.verifyTwoFactorOtp(twoFactorOtp, otp)) {
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Two Factor Authentication Verified!");
            authResponse.setTwoFactorEnabled(true);
            authResponse.setJwt(twoFactorOtp.getJwt());
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        }
        throw new Exception("Invalid token!");
    }
}
