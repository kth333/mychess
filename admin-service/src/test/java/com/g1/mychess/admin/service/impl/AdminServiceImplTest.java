package com.g1.mychess.admin.service.impl;


import com.g1.mychess.admin.client.EmailServiceClient;
import com.g1.mychess.admin.client.PlayerServiceClient;
import com.g1.mychess.admin.dto.AdminPlayerDTO;
import com.g1.mychess.admin.dto.BlacklistDTO;
import com.g1.mychess.admin.dto.UserDTO;
import com.g1.mychess.admin.dto.WhitelistDTO;
import com.g1.mychess.admin.exception.InvalidBlacklistOperationException;
import com.g1.mychess.admin.model.Admin;
import com.g1.mychess.admin.repository.AdminRepository;
import com.g1.mychess.admin.repository.BlacklistRepository;
import com.g1.mychess.admin.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private BlacklistRepository blacklistRepository;

    @Mock
    private PlayerServiceClient playerServiceClient;

    @Mock
    private EmailServiceClient emailServiceClient;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        // Set up any common setup code if required
    }

    @Test
    void test_findAdminByUsername_success() {

        Admin admin = new Admin();
        admin.setUsername("Admin01");
        admin.setPassword("Password123");
        admin.setEmail("Admin01@domain.com");
        admin.setAdminId(1L);

        when(adminRepository.findByUsername("Admin01")).thenReturn(Optional.of(admin));
        UserDTO result = adminService.findAdminByUsername("Admin01");

        verify(adminRepository, times(1)).findByUsername("Admin01");

        assertNotNull(result);
        assertEquals(admin.getUsername(), result.getUsername());
        assertEquals(admin.getPassword(), result.getPassword());
        assertEquals(admin.getEmail(), result.getEmail());
        assertEquals(admin.getRole(), result.getRole());
        assertEquals(admin.getAdminId(), result.getUserId());
    }


    @Test
    void test_findAdminByUsername_NotFound() {
        String username = "NotExist";

        when(adminRepository.findByUsername(username)).thenReturn(Optional.empty());

        Exception expectedException = assertThrows(IllegalArgumentException.class, () ->
                adminService.findAdminByUsername("NotExist"));

        verify(adminRepository, times(1)).findByUsername(username);
        assertEquals("Admin not found with username: " + username, expectedException.getMessage());
    }

    @Test
    void test_blacklistPlayer_AlreadyBlacklisted() {
        BlacklistDTO blacklistDTO = mock(BlacklistDTO.class);

        AdminPlayerDTO adminPlayerDTO = mock(AdminPlayerDTO.class);

        when(playerServiceClient.fetchPlayerDetails(1L)).thenReturn(adminPlayerDTO);
        when(adminPlayerDTO.isBlacklisted()).thenReturn(true);
        when(blacklistDTO.getPlayerId()).thenReturn(1L);

        Exception expectedThrow = assertThrows(InvalidBlacklistOperationException.class, () ->
                adminService.blacklistPlayer(blacklistDTO,request));

        assertEquals( "Player with ID 1 is already blacklisted.",expectedThrow.getMessage());
    }

    @Test
    void test_blacklistPlayer_PlayerNotExist() {
        BlacklistDTO blacklistDTO = mock(BlacklistDTO.class);

        when(blacklistDTO.getPlayerId()).thenReturn(1L);
        when(playerServiceClient.fetchPlayerDetails(1L)).thenReturn(null);

        Exception expectedThrow = assertThrows(IllegalArgumentException.class, () ->
                adminService.blacklistPlayer(blacklistDTO,request));

        assertEquals( "Player not found with username: 1",expectedThrow.getMessage());
    }

    @Test
    void test_whitelistPlayer_NotBlacklisted_ShouldThrowException() {
        WhitelistDTO whitelistDTO = mock(WhitelistDTO.class);
        long playerId = 1L;
        AdminPlayerDTO adminPlayerDTO = mock(AdminPlayerDTO.class);

        when(whitelistDTO.getPlayerId()).thenReturn(playerId);
        when(playerServiceClient.fetchPlayerDetails(playerId)).thenReturn(adminPlayerDTO);
        when(adminPlayerDTO.isBlacklisted()).thenReturn(false);

        Exception expectedThrow = assertThrows(InvalidBlacklistOperationException.class, () ->
                adminService.whitelistPlayer(whitelistDTO,request));

        assertEquals("Player with ID 1 is not blacklisted.",expectedThrow.getMessage());

    }


}
