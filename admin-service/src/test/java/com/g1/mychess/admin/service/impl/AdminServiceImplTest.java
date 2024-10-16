package com.g1.mychess.admin.service;

import com.g1.mychess.admin.dto.*;
import com.g1.mychess.admin.repository.*;
import com.g1.mychess.admin.service.impl.AdminServiceImpl;
import com.g1.mychess.admin.util.*;
import com.g1.mychess.admin.exception.*;
import com.g1.mychess.admin.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminService;
    @Mock
    private BlacklistRepository blacklistRepository;
    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize the mocks
    }

    @Test
    void whitelistNullData_Should_ThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {adminService.whitelistPlayer(null, null));
        });
        assertEquals("Whitelist data must not be null.", exception.getMEssage());
    }

    @Test
    void whitelistNotBlacklisted_Should_ThrowException() {
        WhitelistDTO whitelistDTO = new WhitelistDTO();
        whitelistDTO.setPlayerId(1L);

        AdminPlayerDTO adminPlayerDTO = new AdminPlayerDTO(false, "user1", "user1@example.com");
        when(adminService.fetchPlayerDetails(1L)).thenReturn(adminPlayerDTO);

        Exception exception = assertThrows(InvalidBlacklistOperationException.class,
                () -> {adminService.whitelistPlayer(whitelistDTO, null);
        });
        assertEquals("Player with ID 1 is not blacklisted.", exception.getMessage());
    }

    @Test
    void whitelistSuccessful_Should_UpdateStatus() {
        WhitelistDTO whitelistDTO = new WhitelistDTO();
        whitelistDTO.setPlayerId(1);

        AdminPlayerDTO adminPlayerDTO = new AdminPlayerDTO(false, "user1", "user1@example.com");
        when(adminService.fetchPlayerDetails(1L)).thenReturn(adminPlayerDTO);
        Blacklist blacklist = new Blacklist();
        when(blacklistRepository.findByPlayerId(1L)).thenReturn(Optional.of(blacklist));

        when(jwtUtil.extractUserId("validtoken")).thenReturn(123L);

        adminService.whitelistPlayer(whitelistDTO, null);

        assertFalse(blacklist.isActive());
        assertNotNull(blacklist.getWhitelistedAt());
        assertEquals(Long.valueOf(123), blacklist.getAdminId());
    }

    @Test
    void autoExpiredBansNoActiveBans_Should_DoNothing() {
        when(blacklistRepository.findAllByIsActiveTrue()).thenReturn(Collections.emptyList());

        adminService.autoWhitelistExpiredBans();

        verify(blacklistRepository, never()).save(any());
    }

    @Test
    void autoExpiredBansWithExpiredBan_Should_ProcessEachBan() {
        LocalDateTime now = LocalDateTime.now();
        Blacklist expiredBan = new Blacklist();
        expiredBan.setPlayerId(1L);
        expiredBan.setBlacklistedAt(now.minusHours(1));

        List<Blacklist> expiredBans = Collections.singletonList(expiredBan);
        when(blacklistRepository.findAllByIsActiveTrue()).thenReturn(expiredBans);

        doNothing().when(adminService).whitelistPlayerAfterExpiry(expiredBan);

        adminService.autoWhiteListExpiredBans();

        verify(adminService).whitelistPlayerAfterExpiry(expiredBan);
    }
}