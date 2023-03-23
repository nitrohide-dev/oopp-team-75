package client.utils;

import client.scenes.BoardOverviewCtrl;
import commons.Board;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;

public class MyStompSessionHandler implements StompSessionHandler {

    private StompSession session;
    private BoardOverviewCtrl boc;
    public MyStompSessionHandler(BoardOverviewCtrl boc) {
        this.boc = boc;
    }

    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/topic/messages", this);
        this.session = session;
    }

    public void handleException(StompSession session, StompCommand cmd, StompHeaders hdrs, byte[] payl, Throwable exception) {}

    public void handleTransportError(StompSession session, Throwable exception) {}

    public Type getPayloadType(StompHeaders headers) {
        return Board.class;
    }

    public void handleFrame(StompHeaders headers, Object payload) {
        Board board = (Board) payload;
        boc.update(board);
    }
}
