package com.jorchdev.poketeams.pokegateway.config.websocket;

import com.jorchdev.poketeams.pokegateway.components.websocket.ChatWebSocketProxy;
import com.jorchdev.poketeams.pokegateway.components.websocket.SessionHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WsConfig implements WebSocketConfigurer {

    private final ChatWebSocketProxy chatProxy;
    private final SessionHandshakeInterceptor sessionHandshakeInterceptor;

    public WsConfig(ChatWebSocketProxy chatProxy, SessionHandshakeInterceptor interceptor) {
        this.chatProxy = chatProxy;
        this.sessionHandshakeInterceptor = interceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry r) {
        r.addHandler(chatProxy, "/chat-ws/graphql")
                .addInterceptors(sessionHandshakeInterceptor)
                .setAllowedOrigins("http://localhost:5173");
    }
}