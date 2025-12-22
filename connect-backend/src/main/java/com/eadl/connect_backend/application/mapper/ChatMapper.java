package com.eadl.connect_backend.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.application.dto.response.chat.ConversationResponse;
import com.eadl.connect_backend.application.dto.response.chat.MessageResponse;
import com.eadl.connect_backend.domain.model.chat.Conversation;
import com.eadl.connect_backend.domain.model.chat.Message;

/**
 * Mapper utilitaire pour les objets de messagerie (conversation, message)
 */
public class ChatMapper {

    public static MessageResponse toMessageResponse(Message message) {
        if (message == null) return null;
        MessageResponse dto = new MessageResponse();
        dto.setIdMessage(message.getIdMessage());
        dto.setIdConversation(message.getIdConversation());
        dto.setSenderId(message.getSenderId());
        dto.setType(message.getType());
        dto.setContent(message.getContent());
        dto.setFileUrl(message.getFileUrl());
        dto.setSentAt(message.getSentAt());
        dto.setRead(message.isRead());
        dto.setReadAt(message.getReadAt());
        return dto;
    }

    public static ConversationResponse toConversationResponse(Conversation conversation) {
        return toConversationResponse(conversation, null, null, null, null);
    }

    public static ConversationResponse toConversationResponse(Conversation conversation,
                                                              Message lastMessage,
                                                              Integer unreadCount,
                                                              String clientName,
                                                              String technicianName) {
        if (conversation == null) return null;
        ConversationResponse dto = new ConversationResponse();
        dto.setIdConversation(conversation.getIdConversation());
        dto.setIdReservation(conversation.getIdReservation());
        dto.setIdClient(conversation.getIdClient());
        dto.setClientName(clientName);
        dto.setIdTechnician(conversation.getIdTechnician());
        dto.setTechnicianName(technicianName);
        dto.setCreatedAt(conversation.getCreatedAt());
        dto.setLastMessageAt(conversation.getLastMessageAt());
        dto.setActive(conversation.getActive());
        dto.setUnreadCount(unreadCount);
        dto.setLastMessage(toMessageResponse(lastMessage));
        return dto;
    }

    // Backwards compatible overload for earlier signature (keeps param order simple)
    public static ConversationResponse toConversationResponse(Conversation conversation,
                                                              Message lastMessage,
                                                              Integer unreadCount) {
        return toConversationResponse(conversation, lastMessage, unreadCount, null, null);
    }

    public static List<MessageResponse> toMessageResponses(List<Message> messages) {
        if (messages == null) return null;
        return messages.stream().map(ChatMapper::toMessageResponse).collect(Collectors.toList());
    }

    public static List<ConversationResponse> toConversationResponses(List<Conversation> conversations) {
        if (conversations == null) return null;
        return conversations.stream().map(ChatMapper::toConversationResponse).collect(Collectors.toList());
    }
}

