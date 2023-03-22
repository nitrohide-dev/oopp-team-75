package client.utils;

import commons.Board;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;

public class MyStompSessionHandler implements StompSessionHandler {

    private StompSession session;

    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/topic/messages", this);
        this.session = session;
    }

    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {}

    public void handleTransportError(StompSession session, Throwable exception) {}

    public Type getPayloadType(StompHeaders headers) {
        return Board.class;
    }

    public void handleFrame(StompHeaders headers, Object payload) {
        Board board = (Board) payload;

    }
}
