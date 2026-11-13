package com.jorchdev.poketeams.pokegateway.components.websocket;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class ChatWebSocketProxy extends AbstractWebSocketHandler implements SubProtocolCapable {

    private static final String UPSTREAM = "upstream";
    private final StandardWebSocketClient client = new StandardWebSocketClient();

    @Override
    public List<String> getSubProtocols() {
        return List.of("graphql-transport-ws");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession downstream) throws Exception {
        UUID trainerId = (UUID) downstream.getAttributes().get("trainerId");
        String accessToken = (String) downstream.getAttributes().get("accessToken");

        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

        headers.setSecWebSocketProtocol("graphql-transport-ws");
        headers.setBearerAuth(accessToken);
        headers.add("X-Trainer-Id", trainerId.toString());

        WebSocketSession up = client
                .execute(new UpstreamHandler(downstream), headers, URI.create("ws://localhost:8082/graphql"))
                .get(5, TimeUnit.SECONDS);

        downstream.getAttributes().put(UPSTREAM,
                new ConcurrentWebSocketSessionDecorator(up, 10_000, 512 * 1024));
    }

    @Override
    protected void handleTextMessage(WebSocketSession downstream, @NonNull TextMessage msg) throws Exception {
        WebSocketSession up = (WebSocketSession) downstream.getAttributes().get(UPSTREAM);
        up.sendMessage(msg);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession downstream, @NonNull CloseStatus status) throws Exception {
        CloseStatus s = (status.getCode() == 1005 || status.getCode() == 1006) ? CloseStatus.NORMAL : status;
        WebSocketSession up = (WebSocketSession) downstream.getAttributes().get(UPSTREAM);

        if (up != null && up.isOpen()) up.close(s);
    }

    private static class UpstreamHandler extends AbstractWebSocketHandler {
        private final WebSocketSession downstream;
        UpstreamHandler(WebSocketSession d) { this.downstream = new ConcurrentWebSocketSessionDecorator(d, 10_000, 512 * 1024); }

        @Override
        protected void handleTextMessage(@NonNull WebSocketSession up, @NonNull TextMessage msg) throws Exception {
            downstream.sendMessage(msg);
        }

        @Override
        public void afterConnectionClosed(@NonNull WebSocketSession up, @NonNull CloseStatus status) throws Exception {
            CloseStatus s = (status.getCode() == 1005 || status.getCode() == 1006) ? CloseStatus.NORMAL : status;

            if (downstream.isOpen()) downstream.close(s);
        }
    }
}
