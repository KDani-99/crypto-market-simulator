package cryptogame.model.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AuthTest {
    @Test
    public void testHashPasswordComparisonShouldEqualToPlainTextPassword() throws Exception {
        // Given
        var storedPasswordHash = "$16$2c3f105f8361ca73ce2cc68eb72aeb4f9a2444568a85427ebc5101e0a586e9090b104a7da3ae2204c6d83044af5b6248";
        var givenPlainTextPassword = "myPasswrod23@";

        var expected = true;

        // When
        var comparisonResult = Auth.comparePasswords(storedPasswordHash,givenPlainTextPassword);

        // Then
        assertEquals(expected,comparisonResult);
    }
    @Test
    public void testPasswordGenerationShouldReceiveDifferentHashValuesWithRandomSalt() throws Exception {
        // Given
        String password = "secretPassword";
        boolean expected = false;

        // When
        var firstHash = Auth.generatePasswordHash(password);
        var secondHash = Auth.generatePasswordHash(password);
        var comparisonResult = firstHash.equals(secondHash);

        // Then
        assertEquals(expected,comparisonResult);

    }
}
