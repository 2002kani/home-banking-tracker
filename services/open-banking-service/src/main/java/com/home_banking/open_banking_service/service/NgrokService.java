package com.home_banking.open_banking_service.service;

import com.ngrok.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class NgrokService {

    private Session session;
    private AutoCloseable forwarder;
    private String publicUrl;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> timeoutTask;

    @Value("${ngrok.authToken}")
    private String authToken;

    @Value("${server.port:8080}")
    private int serverPort;

    public synchronized String openTunnel() throws Exception {
        if (forwarder != null) {
            return publicUrl;
        }

        session = Session.withAuthtoken(authToken).connect();

        var ngrokForwarder = session.httpEndpoint()
                .forward(new URL("http://localhost:" + serverPort));

        publicUrl = ngrokForwarder.getUrl();
        forwarder = ngrokForwarder;

        log.info("Ngrok tunnel opened: {}", publicUrl);

        // Security Timeout: Closes automatically after 10 seconds
        timeoutTask = scheduler.schedule(() -> {
            log.warn("Ngrok tunnel auto-closing after timeout");
            closeTunnel();
        }, 10, TimeUnit.MINUTES);

        return publicUrl;
    }

    public void closeTunnelDelayed() {
        scheduler.schedule(this::closeTunnel, 3, TimeUnit.SECONDS);
    }

    public synchronized void closeTunnel() {
        try {
            if (timeoutTask != null) { timeoutTask.cancel(false); timeoutTask = null; }
            if (forwarder != null)   { forwarder.close(); forwarder = null; }
            if (session != null)     { session.close();   session = null;   }
            publicUrl = null;
            log.info("Ngrok tunnel closed.");
        } catch (Exception e) {
            log.warn("Error closing ngrok tunnel", e);
        }
    }
}