package com.eadl.connect_backend.domain.model.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void shouldCreateClientWithCorrectRole() {
        // WHEN
        Client client = Client.create(
                "John",
                "Doe",
                "client@test.com",
                "690000000",
                "password",
                "Douala"
        );

        // THEN
        assertNotNull(client);
        assertEquals(Role.CLIENT, client.getRole());
    }

    @Test
    void shouldBeActiveByDefault() {
        // GIVEN
        Client client = Client.create(
                "Jane",
                "Doe",
                "client@test.com",
                "690000001",
                "password",
                "Yaoundé"
        );

        // THEN
        assertTrue(client.isActive());
    }

    @Test
    void shouldDeactivateClient() {
        // GIVEN
        Client client = Client.create(
                "Jane",
                "Doe",
                "client@test.com",
                "690000001",
                "password",
                "Yaoundé"
        );

        // WHEN
        client.deactivate();

        // THEN
        assertFalse(client.isActive());
    }

    @Test
    void shouldActivateClient() {
        // GIVEN
        Client client = Client.create(
                "Jane",
                "Doe",
                "client@test.com",
                "690000001",
                "password",
                "Yaoundé"
        );
        client.deactivate();

        // WHEN
        client.activate();

        // THEN
        assertTrue(client.isActive());
    }

    @Test
    void toStringShouldContainClientInformation() {
        // GIVEN
        Client client = Client.create(
                "John",
                "Doe",
                "client@test.com",
                "690000000",
                "password",
                "Douala"
        );

        // WHEN
        String result = client.toString();

        // THEN
        assertTrue(result.contains("Client{"));
        assertTrue(result.contains("client@test.com"));
    }
}
