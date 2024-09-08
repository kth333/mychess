package com.g1.mychess.user.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class PlayerTest {

    private Profile profile;

    @BeforeEach
    void setUp() {
        profile = new Profile();
        profile.setUserId(1L);
        profile.setFullName("John Doe");
        profile.setBio("Chess lover.");
        profile.setAvatarUrl("https://example.com/avatar.jpg");
        profile.setGender("Male");
        profile.setCountry("USA");
        profile.setRegion("California");
        profile.setCity("Los Angeles");
        profile.setBirthDate(LocalDate.of(1990, 1, 1));
        profile.setRank(CustomChessRank.MASTER);
        profile.setTotalWins(0);
        profile.setTotalLosses(0);
        profile.setTotalDraws(0);
        profile.setPublic(true);
    }

    // Test cases that should fail based on missing handling in the original class

    @ParameterizedTest
    @ValueSource(strings = {"Phua Chu Kang", "Steven Lim Kor-Kor", "T'Challa"})
    void testValidFullNames(String validName) {
        profile.setFullName(validName); // Should succeed
        assertEquals(validName, profile.getFullName(), "Valid names should be set correctly.");
    }

    @ParameterizedTest
    @CsvSource({
            "'James@Lye!', IllegalArgumentException",
            "'My name is 5', IllegalArgumentException"
    })
    void testInvalidFullNames(String invalidName, Class<? extends Exception> expectedException) {
        Exception exception = assertThrows(expectedException, () -> {
            profile.setFullName(invalidName);
        });
        assertNotNull(exception, "Setting invalid names should throw an exception.");
    }


    @Test
    void testSetTotalWinsNegative() {
        profile.setTotalWins(-1);  // Setting a negative number for total wins should either be validated or handled.
        assertTrue(profile.getTotalWins() >= 0, "Total wins cannot be negative.");
    }

    @Test
    void testSetAndGetBioWithEmptyString() {
        profile.setBio("");  // Setting an empty string for bio might not be validated in the original class.
        assertNotNull(profile.getBio(), "Bio can be an empty string.");
    }

    @Test
    void testSetBirthDateInFuture() {
        profile.setBirthDate(LocalDate.of(2030, 1, 1));  // Setting a future date should be invalid.
        assertTrue(profile.getBirthDate().isBefore(LocalDate.now()), "Birth date should not be in the future.");
    }

    @Test
    void testGetAgeWithNullBirthDate() {
        profile.setBirthDate(null);  // If birthDate is null, the getAge() method will break with a NullPointerException.
        assertThrows(NullPointerException.class, profile::getAge, "getAge() should throw NullPointerException if birthDate is null.");
    }

    @Test
    void testOldestPersonEverLived() {
        int age = profile.getAge();
        assertTrue(age <= 123, "Wow! We have a new world record for the oldest person");
    }

    @Test
    void testSetTotalLossesNegative() {
        profile.setTotalLosses(-1);  // Setting negative losses should either be invalid or handled.
        assertTrue(profile.getTotalLosses() >= 0, "Total losses cannot be negative.");
    }

    @Test
    void testSetTotalDrawsNegative() {
        profile.setTotalDraws(-1);  // Setting negative draws should also be invalid.
        assertTrue(profile.getTotalDraws() >= 0, "Total draws cannot be negative.");
    }

    @Test
    void testSetGenderWithInvalidValue() {
        profile.setGender("Unknown");  // Gender might need validation (e.g., should only allow specific values).
        assertTrue(profile.getGender().equals("Male") || profile.getGender().equals("Female"),
                "Gender should be either Male or Female.");

        profile.setGender("");  // Empty case
        assertTrue(profile.getGender().equals("Male") || profile.getGender().equals("Female"),
                "Gender should be either Male or Female.");
    }

    @Test
    void testSetUserIdNegative() {
        profile.setUserId(-1L);  // User ID should never be negative.
        assertTrue(profile.getUserId() > 0, "User ID should be positive.");
    }


}
