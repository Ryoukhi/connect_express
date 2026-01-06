package com.eadl.connect_backend.domain.model.chat;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class MessageTest {

    @Test
    void shouldCreateTextMessage() {
        Message message = Message.createTextMessage(
                1L,
                10L,
                "Bonjour"
        );

        assertThat(message.getIdMessage()).isNull();
        assertThat(message.getIdConversation()).isEqualTo(1L);
        assertThat(message.getSenderId()).isEqualTo(10L);
        assertThat(message.getType()).isEqualTo(MessageType.TEXT);
        assertThat(message.getContent()).isEqualTo("Bonjour");
        assertThat(message.getFileUrl()).isNull();
        assertThat(message.isRead()).isFalse();
        assertThat(message.getSentAt()).isNotNull();
    }

    @Test
    void shouldCreateImageMessage() {
        Message message = Message.createImageMessage(
                1L,
                10L,
                "Photo du problème",
                "https://cdn.app/images/img.png"
        );

        assertThat(message.getType()).isEqualTo(MessageType.IMAGE);
        assertThat(message.getContent()).contains("Photo");
        assertThat(message.getFileUrl()).contains("img.png");
        assertThat(message.isRead()).isFalse();
    }

    @Test
    void shouldCreateFileMessage() {
        Message message = Message.createFileMessage(
                1L,
                10L,
                "devis.pdf",
                "https://cdn.app/files/devis.pdf"
        );

        assertThat(message.getType()).isEqualTo(MessageType.FILE);
        assertThat(message.getContent()).isEqualTo("devis.pdf");
        assertThat(message.getFileUrl()).endsWith(".pdf");
        assertThat(message.isRead()).isFalse();
    }

    @Test
    void shouldCreateSystemMessage() {
        Message message = Message.createSystemMessage(
                1L,
                "Conversation clôturée"
        );

        assertThat(message.getType()).isEqualTo(MessageType.SYSTEM);
        assertThat(message.getSenderId()).isNull();
        assertThat(message.isRead()).isTrue();
        assertThat(message.getReadAt()).isNull(); // pas défini à la création
    }

    @Test
    void shouldMarkMessageAsRead() throws InterruptedException {
        Message message = Message.createTextMessage(
                1L,
                10L,
                "Hello"
        );

        assertThat(message.isRead()).isFalse();

        Thread.sleep(5);
        message.markAsRead();

        assertThat(message.isRead()).isTrue();
        assertThat(message.getReadAt()).isNotNull();
    }

    @Test
    void shouldNotOverrideReadDateIfAlreadyRead() {
        Message message = Message.createTextMessage(
                1L,
                10L,
                "Hello"
        );

        message.markAsRead();
        LocalDateTime firstReadAt = message.getReadAt();

        message.markAsRead();

        assertThat(message.getReadAt()).isEqualTo(firstReadAt);
    }

    @Test
    void shouldDetectSystemMessage() {
        Message message = Message.createSystemMessage(
                1L,
                "Système"
        );

        assertThat(message.isSystemMessage()).isTrue();
    }

    @Test
    void shouldDetectAttachment() {
        Message imageMessage = Message.createImageMessage(
                1L,
                10L,
                "Image",
                "http://file.png"
        );

        Message textMessage = Message.createTextMessage(
                1L,
                10L,
                "Simple texte"
        );

        assertThat(imageMessage.hasAttachment()).isTrue();
        assertThat(textMessage.hasAttachment()).isFalse();
    }

    @Test
    void shouldCheckSender() {
        Message message = Message.createTextMessage(
                1L,
                10L,
                "Hello"
        );

        assertThat(message.isSentBy(10L)).isTrue();
        assertThat(message.isSentBy(99L)).isFalse();
    }

    @Test
    void shouldRespectEqualsAndHashCode() {
        Message message1 = Message.createTextMessage(1L, 10L, "A");
        Message message2 = Message.createTextMessage(1L, 10L, "B");

        message1.setIdMessage(100L);
        message2.setIdMessage(100L);

        assertThat(message1).isEqualTo(message2);
        assertThat(message1.hashCode()).isEqualTo(message2.hashCode());
    }

    @Test
    void shouldNotBeEqualIfIdDifferent() {
        Message message1 = Message.createTextMessage(1L, 10L, "A");
        Message message2 = Message.createTextMessage(1L, 10L, "B");

        message1.setIdMessage(1L);
        message2.setIdMessage(2L);

        assertThat(message1).isNotEqualTo(message2);
    }

    @Test
    void shouldNotBeEqualIfIdIsNull() {
        Message message1 = Message.createTextMessage(1L, 10L, "A");
        Message message2 = Message.createTextMessage(1L, 10L, "A");

        assertThat(message1).isNotEqualTo(message2);
    }
}
