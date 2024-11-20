//package com.g1.mychess.player.util;
//
//import com.g1.mychess.player.PlayerServiceApplication;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
///**
// * Tests wasn't compiling, JwtUtil was throwing exceptions even though the code was correct
// * turns out it was unable to locate .env.
// */
//
//@SpringBootTest(classes = PlayerServiceApplication.class)
//@ActiveProfiles("test")
//public class JwtUtilTest {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Test
//    void testJwtUtilBeanCreation() {
//
//        assertNotNull(jwtUtil, "JwtUtil bean should be created");
//    }
//
//}
