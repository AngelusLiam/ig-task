package com.ig.demo.event;

import com.ig.demo.event.UserCreatedEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class UserEventListener {

    @EventListener
    @Async
    public void handleUserCreatedEvent(UserCreatedEvent event) {

        log.info("Creazione utente: {}, email: {}", event.getUsername(), event.getEmail());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("Elaborazione completata per l'utente: {}", event.getUsername());
        int height = 5;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < height - i - 1; j++) {
                System.out.print(" ");
            }
            for (int k = 0; k < 2 * i + 1; k++) {
                if (k % 2 == 0) {
                    System.out.print("*");
                } else {
                    System.out.print("o");
                }
            }
            System.out.println();
        }
        for (int i = 0; i < height - 1; i++) {
            System.out.print(" ");
        }
        System.out.println("|");
        log.info("Buone feste!");
    }
}
