package com.eadl.connect_backend.domain.model.chat;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ConversationTest {

    @Test
    void shouldCreateConversationWithDefaultValues() {
        Conversation conversation = Conversation.create(
                10L,
                1L,
                2L
        );

        assertThat(conversation.getIdConversation()).isNull();
        assertThat(conversation.getIdReservation()).isEqualTo(10L);
        assertThat(conversation.getIdClient()).isEqualTo(1L);
        assertThat(conversation.getIdTechnician()).isEqualTo(2L);
        assertThat(conversation.isActive()).isTrue();

        assertThat(conversation.getCreatedAt()).isNotNull();
        assertThat(conversation.getLastMessageAt()).isNotNull();
    }

    @Test
    void shouldUpdateLastMessageTime() throws InterruptedException {
        Conversation conversation = Conversation.create(
                10L,
                1L,
                2L
        );

        LocalDateTime beforeUpdate = conversation.getLastMessageAt();

        Thread.sleep(5); // garantit un delta temporel
        conversation.updateLastMessageTime();

        assertThat(conversation.getLastMessageAt()).isAfter(beforeUpdate);
    }

    @Test
    void shouldCloseAndReopenConversation() {
        Conversation conversation = Conversation.create(
                10L,
                1L,
                2L
        );

        conversation.close();
        assertThat(conversation.isActive()).isFalse();

        conversation.reopen();
        assertThat(conversation.isActive()).isTrue();
    }

    @Test
    void shouldReturnTrueWhenUserIsParticipant() {
        Conversation conversation = Conversation.create(
                10L,
                1L,
                2L
        );

        assertThat(conversation.isParticipant(1L)).isTrue(); // client
        assertThat(conversation.isParticipant(2L)).isTrue(); // technicien
    }

    @Test
    void shouldReturnFalseWhenUserIsNotParticipant() {
        Conversation conversation = Conversation.create(
                10L,
                1L,
                2L
        );

        assertThat(conversation.isParticipant(99L)).isFalse();
    }

    @Test
    void shouldRespectEqualsAndHashCode() {
        Conversation conversation1 = Conversation.create(10L, 1L, 2L);
        Conversation conversation2 = Conversation.create(10L, 1L, 2L);

        conversation1.setIdConversation(100L);
        conversation2.setIdConversation(100L);

        assertThat(conversation1).isEqualTo(conversation2);
        assertThat(conversation1.hashCode()).isEqualTo(conversation2.hashCode());
    }

    @Test
    void shouldNotBeEqualIfIdDifferent() {
        Conversation conversation1 = Conversation.create(10L, 1L, 2L);
        Conversation conversation2 = Conversation.create(10L, 1L, 2L);

        conversation1.setIdConversation(1L);
        conversation2.setIdConversation(2L);

        assertThat(conversation1).isNotEqualTo(conversation2);
    }

    @Test
    void shouldNotBeEqualIfIdIsNull() {
        Conversation conversation1 = Conversation.create(10L, 1L, 2L);
        Conversation conversation2 = Conversation.create(10L, 1L, 2L);

        assertThat(conversation1).isNotEqualTo(conversation2);
    }
}

