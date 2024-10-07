package com.g1.mychess.auth.service.impl;

import com.g1.mychess.auth.dto.*;
import com.g1.mychess.auth.exception.*;
import com.g1.mychess.auth.model.UserToken;
import com.g1.mychess.auth.repository.UserTokenRepository;
import com.g1.mychess.auth.service.AuthService;
import com.g1.mychess.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final WebClient.Builder webClientBuilder;
    private final PasswordEncoder passwordEncoder;
    private final UserTokenRepository userTokenRepository;
    private final JwtUtil jwtUtil;

    @Value("${player.service.url}")
    private String playerServiceUrl;

    @Value("${admin.service.url}")
    private String adminServiceUrl;

    @Value("${email.service.url}")
    private String emailServiceUrl;

    public AuthServiceImpl(WebClient.Builder webClientBuilder, PasswordEncoder passwordEncoder, UserTokenRepository userTokenRepository, JwtUtil jwtUtil) {
        this.webClientBuilder = webClientBuilder;
        this.passwordEncoder = passwordEncoder;
        this.userTokenRepository = userTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<String> registerUser(RegisterRequestDTO registerRequestDTO) {
        String password = registerRequestDTO.getPassword();

        if (isValidPassword(password)) {
            throw new InvalidPasswordException("Password must be at least 8 characters long and contain at least one number.");
        }

        String email = registerRequestDTO.getEmail();
        if (!isValidEmail(email)) {
            throw new InvalidEmailException("This is not a valid email address.");
        }

        String hashedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());

        RegisterRequestDTO playerDTO = new RegisterRequestDTO(
                registerRequestDTO.getUsername(),
                hashedPassword,
                registerRequestDTO.getEmail(),
                registerRequestDTO.getGender(),
                registerRequestDTO.getCountry(),
                registerRequestDTO.getRegion(),
                registerRequestDTO.getCity(),
                registerRequestDTO.getBirthDate()
        );

        ResponseEntity<PlayerCreationResponseDTO> playerServiceResponse = createPlayerInPlayerService(playerDTO);

        if (playerServiceResponse == null) {
            throw new PlayerServiceException("No response from player service.");
        }

        if (playerServiceResponse.getStatusCode().is2xxSuccessful()) {
            PlayerCreationResponseDTO responseBody = playerServiceResponse.getBody();

            if (responseBody != null && responseBody.getPlayerId() != null) {
                String verificationToken = generateToken(responseBody.getPlayerId(), "ROLE_PLAYER", UserToken.TokenType.EMAIL_VERIFICATION, LocalDateTime.now().plusDays(1));
                try {
                    sendVerificationEmail(registerRequestDTO.getEmail(), registerRequestDTO.getUsername(), verificationToken);
                } catch (Exception e) {
                    throw new EmailSendFailedException("Failed to send verification email.");
                }
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
        UserDTO userDTO;

        if (role.equals("ROLE_PLAYER")) {
            userDTO = fetchPlayerFromPlayerService(username);
        } else if (role.equals("ROLE_ADMIN")) {
            userDTO = fetchAdminFromAdminService(username);
        } else {
            throw new InvalidRoleException("Invalid role provided.");
        }

        if (userDTO == null) {
            throw new UserNotFoundException("User not found.");
        }

        if (!passwordEncoder.matches(password, userDTO.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }

        Long userId = userDTO.getUserId();

        if (!isEmailVerified(userId, role)) {
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
        UserDTO userDTO = fetchPlayerFromPlayerServiceByEmail(email);

        if (userDTO == null) {
            throw new UserNotFoundException("User with this email not found.");
        }

        Long userId = userDTO.getUserId();

        if (isEmailVerified(userId, userDTO.getRole())) {
            throw new EmailAlreadyVerifiedException("Your email is already verified. Proceed to login!");
        }

        String verificationToken = generateToken(userId, userDTO.getRole(), UserToken.TokenType.EMAIL_VERIFICATION, LocalDateTime.now().plusDays(1));
        try {
            sendVerificationEmail(userDTO.getEmail(), userDTO.getUsername(), verificationToken);
        } catch (Exception e) {
            throw new EmailSendFailedException("Failed to send verification email.");
        }

        return ResponseEntity.ok("Verification email resent successfully.");
    }

    @Override
    public ResponseEntity<String> requestPasswordReset(String email) {
        UserDTO userDTO = fetchPlayerFromPlayerServiceByEmail(email);

        if (userDTO == null) {
            throw new UserNotFoundException("User not found.");
        }

        Long userId = userDTO.getUserId();
        String resetToken = generateToken(userId, userDTO.getRole(), UserToken.TokenType.PASSWORD_RESET, LocalDateTime.now().plusHours(1));

        try {
            sendVerificationEmail(userDTO.getEmail(), userDTO.getUsername(), resetToken);
        } catch (Exception e) {
            throw new EmailSendFailedException("Failed to send password reset email.");
        }

        return ResponseEntity.ok("Password reset email sent successfully.");
    }

    @Override
    public ResponseEntity<String> resetPassword(String resetToken, String newPassword) {
        if (isValidPassword(newPassword)) {
            throw new InvalidPasswordException("Password does not meet the requirements.");
        }

        UserToken token = userTokenRepository.findByTokenAndTokenType(resetToken, UserToken.TokenType.PASSWORD_RESET)
                .orElseThrow(() -> new UserTokenException("Invalid reset token."));

        if (token.isUsed()) {
            throw new UserTokenException("Reset token already used.");
        }

        if (token.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new UserTokenException("Token has expired. Please request password reset again.");
        }

        UserDTO userDTO = fetchPlayerFromPlayerServiceById(token.getUserId());

        if (userDTO == null) {
            throw new UserNotFoundException("User not found.");
        }

        String hashedPassword = passwordEncoder.encode(newPassword);

        updatePasswordInPlayerService(userDTO.getUserId(), hashedPassword);

        token.setUsed(true);
        userTokenRepository.save(token);

        return ResponseEntity.ok("Password has been reset successfully.");
    }

    @Override
    public void verifyEmail(String token) {
        UserToken userToken = userTokenRepository.findByTokenAndTokenType(token, UserToken.TokenType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new UserTokenException("Token is invalid."));

        if (userToken.isUsed()) {
            throw new UserTokenException("Your email is already verified. You can proceed to log in.");
        }

        if (userToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new UserTokenException("Token is expired. Please request a new verification email.");
        }

        userToken.setUsed(true);
        userTokenRepository.save(userToken);
    }

    @Override
    public boolean isEmailVerified(Long userId, String userType) {
        UserToken userToken = userTokenRepository.findByUserIdAndUserTypeAndTokenTypeAndUsed(
                userId,
                userType,
                UserToken.TokenType.EMAIL_VERIFICATION,
                true
        ).orElse(null);

        return userToken != null;
    }

    private String generateToken(Long userId, String userType, UserToken.TokenType tokenType, LocalDateTime expirationTime) {
        try {
            Optional<UserToken> existingToken = userTokenRepository.findByUserIdAndUserTypeAndTokenType(userId, userType, tokenType);

            existingToken.ifPresent(userTokenRepository::delete);

            String token = UUID.randomUUID().toString();

            UserToken userToken = new UserToken(
                    token,
                    expirationTime,
                    tokenType,
                    userId,
                    userType
            );

            userTokenRepository.save(userToken);

            return token;
        } catch (Exception e) {
            throw new UserTokenException("Failed to generate or store verification token.");
        }
    }

    private void sendVerificationEmail(String email, String username, String verificationToken) {
        EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
        emailRequestDTO.setTo(email);
        emailRequestDTO.setUsername(username);
        emailRequestDTO.setUserToken(verificationToken);

        try {
            webClientBuilder.build()
                    .post()
                    .uri(emailServiceUrl + "/api/v1/email/send-verification")
                    .bodyValue(emailRequestDTO)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new EmailSendFailedException("Failed to send the verification email.");
        }
    }

    private ResponseEntity<PlayerCreationResponseDTO> createPlayerInPlayerService(RegisterRequestDTO playerDTO) {
        return webClientBuilder.build()
                .post()
                .uri(playerServiceUrl + "/api/v1/player/create")
                .bodyValue(playerDTO)
                .retrieve()
                .toEntity(PlayerCreationResponseDTO.class)
                .block();
    }

    private UserDTO fetchPlayerFromPlayerService(String username) {
        return webClientBuilder.build()
                .get()
                .uri(playerServiceUrl + "/api/v1/player/username/" + username)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

    private UserDTO fetchAdminFromAdminService(String username) {
        return webClientBuilder.build()
                .get()
                .uri(adminServiceUrl + "/api/v1/admin/username/" + username)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

    private UserDTO fetchPlayerFromPlayerServiceByEmail(String email) {
        return webClientBuilder.build()
                .get()
                .uri(playerServiceUrl + "/api/v1/player/email/" + email)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

    private UserDTO fetchPlayerFromPlayerServiceById(Long playerId) {
        return webClientBuilder.build()
                .get()
                .uri(playerServiceUrl + "/api/v1/player/playerId/" + playerId)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

    private void updatePasswordInPlayerService(Long playerId, String hashedPassword) {
        UpdatePasswordRequestDTO updatePasswordRequest = new UpdatePasswordRequestDTO(playerId, hashedPassword);

        try {
            webClientBuilder.build()
                    .put()
                    .uri(playerServiceUrl + "/api/v1/player/update-password")
                    .bodyValue(updatePasswordRequest)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            throw new PlayerServiceException("Failed to update the user's password.");
        }
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*\\d.*");
    }

    public boolean isValidEmail(String email) {
        String EMAIL_REGEX = "(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[!#$%&'*+/=?^_`{|}~\\-\\x20-\\x7E]|\\\\[!#$%&'*+/=?^_`{|}~\\-\\x20-\\x7E])*\")@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";
        return email.matches(EMAIL_REGEX);
    }
}