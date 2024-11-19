package com.moreira.picpaychallenge.application.services;

import com.moreira.picpaychallenge.domain.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;
    private final RestTemplate restTemplate;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${api.base.url}")
    private String apiBaseUrl;

    public NotificationService(JavaMailSender mailSender, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.mailSender = mailSender;
    }

    public void sendTransferNotification(User sender, User receiver, BigDecimal amount) {
        //enviar email real para o remetente e destinatário
        sendNotificationEmail(sender, receiver, amount);

        //enviando para o mock de notificação
        sendMockNotification(sender, receiver, amount);
    }

    private void sendMockNotification(User sender, User receiver, BigDecimal amount) {
        String url = apiBaseUrl + "/api/v1/notify";

        //criando corpo da requisição
        //inicialmente será Map e será convertido para JSON pelo RestTemplate
                                        //"Map.of" cria um mapa imutável

        //eventualmente a key2 (data) terá como value outro Map imutável, que será para passar a mensagem no corpo de
        //resposta do JSON
        Map<String, Object> notificationData = Map.of(
                "status", "success",
                "data", Map.of("message", "Transferência de R$ " + amount + " de " + sender.getFullName()
                + " para " + receiver.getFullName() + " concluída com sucesso.")
        );

        //config cabeçalho HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //criando entidade http com os dados e cabeçalhos
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(notificationData, headers);

        //enviando requisição post
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Notificação enviado para o mock.");
            } else {
                System.err.println("Falha ao enviar a notificação para o mock: " + response.getBody());
            }
        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação para o mockk: " + e.getMessage());
        }
    }

    public void sendNotificationEmail(User sender, User receiver, BigDecimal amount) {
        //email para remetente
        SimpleMailMessage senderMessage = new SimpleMailMessage();
        senderMessage.setTo(sender.getEmail());
        senderMessage.setSubject("Transferência concluída.");
        senderMessage.setText("Olá " + sender.getFullName() + ", você enviou R$ " + amount + " para " + receiver.getFullName());
        mailSender.send(senderMessage);

        //email para destinatário

        SimpleMailMessage receiverMessage = new SimpleMailMessage();
        senderMessage.setTo(receiver.getEmail());
        senderMessage.setSubject("Transferência recebida.");
        senderMessage.setText("Olá " + receiver.getFullName() + ", você recebeu R$ " + amount + " de " + sender.getFullName());
        mailSender.send(receiverMessage);
    }
}
