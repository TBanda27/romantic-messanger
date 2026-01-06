package com.romantic_messenger.romanticmessenger.claude.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collections;

@Service
@Slf4j
public class TwilioService {

    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;

    public void sendMMS(String toPhoneNumber, String audioUrl){
        // Normalize phone number - ensure it starts with +
        String normalizedPhoneNumber = normalizePhoneNumber(toPhoneNumber);
        log.info("Sending MMS to {} ", normalizedPhoneNumber);

        Message message = Message.creator(
                new PhoneNumber(normalizedPhoneNumber),
                new PhoneNumber(twilioPhoneNumber),
                Collections.singletonList(URI.create(audioUrl))
        ).create();
        log.info("MMS sent with SID: {}", message.getSid());
    }

    private String normalizePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
        // Remove any whitespace
        String cleaned = phoneNumber.trim().replaceAll("\\s+", "");
        // If it doesn't start with +, add it
        if (!cleaned.startsWith("+")) {
            cleaned = "+" + cleaned;
        }
        return cleaned;
    }
}
