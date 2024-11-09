package com.g1.mychess.auth.service.impl;

import com.g1.mychess.auth.client.AdminServiceClient;
import com.g1.mychess.auth.client.EmailServiceClient;
import com.g1.mychess.auth.client.PlayerServiceClient;
import com.g1.mychess.auth.dto.*;
import com.g1.mychess.auth.exception.*;
import com.g1.mychess.auth.model.UserToken;
import com.g1.mychess.auth.service.AuthService;
import com.g1.mychess.auth.service.TokenService;
import com.g1.mychess.auth.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PlayerServiceClient playerServiceClient;
    private final AdminServiceClient adminServiceClient;
    private final EmailServiceClient emailServiceClient;
    private final TokenService tokenService;

    public AuthServiceImpl(
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            PlayerServiceClient playerServiceClient,
            AdminServiceClient adminServiceClient,
            EmailServiceClient emailServiceClient,
            TokenService tokenService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.playerServiceClient = playerServiceClient;
        this.adminServiceClient = adminServiceClient;
        this.emailServiceClient = emailServiceClient;
        this.tokenService = tokenService;
    }

    @Override
    public ResponseEntity<String> registerUser(RegisterRequestDTO registerRequestDTO) {
        String password = registerRequestDTO.getPassword();

        if (!isValidPassword(password)) {
            throw new InvalidPasswordException("Password must be at least 8 characters long and contain at least one number.");
        }

        String email = registerRequestDTO.getEmail();
        if (!isValidEmail(email)) {
            throw new InvalidEmailException("This is not a valid email address.");
        }

        String hashedPassword = passwordEncoder.encode(password);

        RegisterRequestDTO playerDTO = new RegisterRequestDTO(
                registerRequestDTO.getUsername(),
                hashedPassword,
                email,
                registerRequestDTO.getGender(),
                registerRequestDTO.getCountry(),
                registerRequestDTO.getRegion(),
                registerRequestDTO.getCity(),
                registerRequestDTO.getBirthDate()
        );

        ResponseEntity<PlayerCreationResponseDTO> playerServiceResponse = playerServiceClient.createPlayer(playerDTO);

        if (playerServiceResponse == null) {
            throw new PlayerServiceException("No response from player service.");
        }

        if (playerServiceResponse.getStatusCode().is2xxSuccessful()) {
            PlayerCreationResponseDTO responseBody = playerServiceResponse.getBody();

            if (responseBody != null && responseBody.getPlayerId() != null) {
                String verificationToken = tokenService.generateToken(responseBody.getPlayerId(), "ROLE_PLAYER", UserToken.TokenType.EMAIL_VERIFICATION, LocalDateTime.now().plusDays(1));

                sendVerificationEmail(email, registerRequestDTO.getUsername(), verificationToken);

                return ResponseEntity.ok("Registration successful! Check your email to verify your account.");
            } else {
                throw new PlayerServiceException("userId is missing in the response from the player service.");
            }
        }

        if (playerServiceResponse.getStatusCode() == HttpStatus.CONFLICT) {
            PlayerCreationResponseDTO responseBody = playerServiceResponse.getBody();
            if (responseBody != null) {
                throw new UserAlreadyExistsException(responseBody.getMessage());
            }
        }

        throw new PlayerServiceException("Player service failed to register the user. Status code: " + playerServiceResponse.getStatusCode());
    }

    @Override
    public String login(String username, String password, String role) {
        UserDTO userDTO = fetchUserByUsernameAndRole(username, role);

        if (!passwordEncoder.matches(password, userDTO.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }

        Long userId = userDTO.getUserId();

        if (!tokenService.isEmailVerified(userId, role)) {
            throw new EmailNotVerifiedException("Please verify your email before logging in.");
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                userDTO.getUsername(),
                userDTO.getPassword(),
                Collections.singleton(new org.springframework.security.core.authority.SimpleGrantedAuthority(role))
        );

        return jwtUtil.generateToken(userDetails, userDTO.getUserId());
    }

    @Override
    public ResponseEntity<String> resendVerificationEmail(String email) {
        UserDTO userDTO = fetchUserByEmail(email);

        if (tokenService.isEmailVerified(userDTO.getUserId(), userDTO.getRole())) {
            throw new EmailAlreadyVerifiedException("Your email is already verified. Proceed to login!");
        }

        String verificationToken = tokenService.generateToken(userDTO.getUserId(), userDTO.getRole(), UserToken.TokenType.EMAIL_VERIFICATION, LocalDateTime.now().plusDays(1));

        sendVerificationEmail(userDTO.getEmail(), userDTO.getUsername(), verificationToken);

        return ResponseEntity.ok("Verification email resent successfully.");
    }

    @Override
    public ResponseEntity<String> requestPasswordReset(String email) {
        UserDTO userDTO = fetchUserByEmail(email);

        String resetToken = tokenService.generateToken(userDTO.getUserId(), userDTO.getRole(), UserToken.TokenType.PASSWORD_RESET, LocalDateTime.now().plusHours(1));

        sendPasswordResetEmail(userDTO.getEmail(), userDTO.getUsername(), resetToken);

        return ResponseEntity.ok("Password reset email sent successfully.");
    }

    @Override
    public ResponseEntity<String> resetPassword(String resetToken, String newPassword) {
        if (!isValidPassword(newPassword)) {
            throw new InvalidPasswordException("Password does not meet the requirements.");
        }

        UserToken token = tokenService.validateToken(resetToken, UserToken.TokenType.PASSWORD_RESET);

        UserDTO userDTO = fetchUserById(token.getUserId(), token.getUserType());

        String hashedPassword = passwordEncoder.encode(newPassword);

        updatePassword(userDTO.getUserId(), hashedPassword);

        tokenService.markTokenAsUsed(token);

        return ResponseEntity.ok("Password has been reset successfully.");
    }

    @Override
    public void verifyEmail(String token) {
        UserToken userToken = tokenService.validateToken(token, UserToken.TokenType.EMAIL_VERIFICATION);
        tokenService.markTokenAsUsed(userToken);
    }

    @Override
    public boolean isEmailVerified(Long userId, String userType) {
        return tokenService.isEmailVerified(userId, userType);
    }

    // Private helper methods to reduce duplication

    private UserDTO fetchUserByUsernameAndRole(String username, String role) {
        UserDTO userDTO;
        if ("ROLE_PLAYER".equals(role)) {
            userDTO = playerServiceClient.fetchPlayerByUsername(username);
        } else if ("ROLE_ADMIN".equals(role)) {
            userDTO = adminServiceClient.fetchAdminByUsername(username);
        } else {
            throw new InvalidRoleException("Invalid role provided.");
        }

        if (userDTO == null) {
            throw new UserNotFoundException("User not found.");
        }

        return userDTO;
    }

    private UserDTO fetchUserByEmail(String email) {
        UserDTO userDTO = playerServiceClient.fetchPlayerByEmail(email);

        if (userDTO == null) {
            throw new UserNotFoundException("User with this email not found.");
        }

        return userDTO;
    }

    private UserDTO fetchUserById(Long userId, String role) {
        UserDTO userDTO;
        if ("ROLE_PLAYER".equals(role)) {
            userDTO = playerServiceClient.fetchPlayerById(userId);
        } /* else if ("ROLE_ADMIN".equals(role)) {
            userDTO = adminServiceClient.fetchAdminById(userId);
        } */ else {
            throw new InvalidRoleException("Invalid role provided.");
        }

        if (userDTO == null) {
            throw new UserNotFoundException("User not found.");
        }

        return userDTO;
    }

    private void updatePassword(Long playerId, String hashedPassword) {
        playerServiceClient.updatePassword(playerId, hashedPassword);
    }

    private void sendVerificationEmail(String email, String username, String verificationToken) {
        EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
        emailRequestDTO.setTo(email);
        emailRequestDTO.setUsername(username);
        emailRequestDTO.setUserToken(verificationToken);
        emailServiceClient.sendVerificationEmail(emailRequestDTO);
    }

    private void sendPasswordResetEmail(String email, String username, String resetToken) {
        EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
        emailRequestDTO.setTo(email);
        emailRequestDTO.setUsername(username);
        emailRequestDTO.setUserToken(resetToken);
        emailServiceClient.sendPasswordResetEmail(emailRequestDTO);
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*\\d.*");
    }

    public boolean isValidEmail(String email) {
        String EMAIL_REGEX = "(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+" +
                "(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|" +
                "\"(?:[!#$%&'*+/=?^_`{|}~\\-\\x20-\\x7E]|" +
                "\\\\[!#$%&'*+/=?^_`{|}~\\-\\x20-\\x7E])*\")@" +
                "(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+" +
                "[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";
        return email.matches(EMAIL_REGEX);
    }
}