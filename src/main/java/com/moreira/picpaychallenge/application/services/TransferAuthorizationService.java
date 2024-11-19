package com.moreira.picpaychallenge.application.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.moreira.picpaychallenge.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class TransferAuthorizationService {

    private final RestTemplate restTemplate;

    @Value("${api.base.url}")
    private String apiBaseUrl;

    //Para o RestTemplate ser injetado por construtor, foi declarado em "AppConfig"
    public TransferAuthorizationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean authorizeTransfer(Long senderId, Long receiverId, BigDecimal amount) {
        //url para consultar passando parâmetro
        String url = apiBaseUrl + "/api/v2/authorize";

        try {
            //realizando chamada GET para api externa com RestTemplate
            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, null, JsonNode.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode rootNode = response.getBody();
                JsonNode statusNode = rootNode.path("status"); //.path ou .get

                //navega pela estrutura do JSON da api até o campo de authorization
                JsonNode authorizationNode = rootNode.path("data").path("authorization");

                if ("success".equals(statusNode.asText()) && authorizationNode.asBoolean()) {
                    return true;
                } else {
                    return false;
                }

            } else {
                // Se o status da resposta não for OK (200), retorna 'false'
                System.err.println("Falha na requisição de autorização: " + response.getStatusCode());
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error authorizing the transfer: " + e.getMessage());
            return false;
        }
    }

}
