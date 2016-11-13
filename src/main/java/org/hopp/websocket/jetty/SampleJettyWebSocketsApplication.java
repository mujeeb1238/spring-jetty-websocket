package org.hopp.websocket.jetty;

import org.hopp.websocket.jetty.client.GreetingService;
import org.hopp.websocket.jetty.client.SimpleGreetingService;
import org.hopp.websocket.jetty.echo.DefaultEchoService;
import org.hopp.websocket.jetty.echo.EchoService;
import org.hopp.websocket.jetty.echo.EchoWebSocketHandler;
import org.hopp.websocket.jetty.reverse.ReverseWebSocketEndpoint;
import org.hopp.websocket.jetty.snake.SnakeWebSocketHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableAutoConfiguration
@EnableWebSocket
public class SampleJettyWebSocketsApplication extends SpringBootServletInitializer
		implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		/*
		* This was not working because of .withSockJS();
		registry.addHandler(echoWebSocketHandler(), "/echo").withSockJS();
		registry.addHandler(snakeWebSocketHandler(), "/snake").withSockJS();
		*/
		registry.addHandler(echoWebSocketHandler(), "/echo");
		registry.addHandler(snakeWebSocketHandler(), "/snake");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SampleJettyWebSocketsApplication.class);
	}

	@Bean
	public EchoService echoService() {
		return new DefaultEchoService("Did you say \"%s\"?");
	}

	@Bean
	public GreetingService greetingService() {
		return new SimpleGreetingService();
	}

	@Bean
	public WebSocketHandler echoWebSocketHandler() {
		return new EchoWebSocketHandler(echoService());
	}

	@Bean
	public WebSocketHandler snakeWebSocketHandler() {
		return new PerConnectionWebSocketHandler(SnakeWebSocketHandler.class);
	}

	@Bean
	public ReverseWebSocketEndpoint reverseWebSocketEndpoint() {
		return new ReverseWebSocketEndpoint();
	}

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

	public static void main(String[] args) {
		SpringApplication.run(SampleJettyWebSocketsApplication.class, args);
	}

}
