package com.ssafy.shieldron.global.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
public class SmsAuthUtil {

    private final Random rand = new Random();
    private DefaultMessageService messageService;


    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @PostConstruct
    public void init() {
        this.messageService = new DefaultMessageService(apiKey, apiSecret, "https://api.solapi.com");
    }

    @Value("${coolsms.api.number}")
    private String fromPhoneNumber;

    public String sendSms(String to) throws Exception {
        String code = generateCode();
        Message message = new Message();
        message.setFrom(fromPhoneNumber);
        message.setTo(to);
        message.setText("Shieldrone 인증 코드입니다. [" + code + "]");
        try {
            messageService.send(message);
        } catch (Exception e) {
            log.error("인증 문자 전송 실패 : {}", to);
            throw new Exception("SMS 전송 실패", e);
        }
        return code;
    }

    private String generateCode() {
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            numStr.append(rand.nextInt(10));
        }
        return numStr.toString();
    }
}
