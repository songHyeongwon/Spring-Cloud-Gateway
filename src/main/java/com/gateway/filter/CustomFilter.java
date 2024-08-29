package com.gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter() {
        super(Config.class);
    }
    @Data
    public static class Config {
    }
    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            //ServerHttpResponse response = exchange.getResponse();

            log.info("config {}" , config);
            log.info("request {}" , request.getPath());
            return chain.filter(exchange).then(Mono.fromRunnable( () -> {
                ServerHttpResponse response = exchange.getResponse();
                log.info("response {}" , response.getStatusCode());
            }));
        }));
    }

}
