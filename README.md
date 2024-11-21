# Tecnológias utilizadas

- Spring Web
- Spring Data JPA
- Lombok (para códigos boilerplate)
- Spring DevTools
- H2 Database ou algum banco relacional

[Link desafio](https://github.com/PicPay/picpay-desafio-backend)

# Entidades

1. [ ] Teremos dois tipos de usuários: comuns e lojistas.

2. [ ] Ambos terão ``Nome Completo``, ``CPF``, ``e-mail`` e ``senha``;

3. [ ] CPF/CNPJ e e-mail serão únicos. Ou seja, o sistema deverá permitir apenas um cadastro com o mesmo CPF ou endereço
de email;

4. [ ] Usuários podem enviar dinheiro (transferência) para lojistas e entre usuários;

5. [ ] Lojistas só recebem transferências, não enviam dinheiro para ninguém;

6. [ ] Validar se o usuário tem saldo antes da transferência;

7. [ ] Antes de finalizar a transferência, deve-se consultar um serviço autorizador externo, use este mock 
https://util.devi.tools/api/v2/authorize para simular o serviço utilizando o verbo GET;

8. [ ] A operação de transferência deve ser uma transação (ou seja, revertida em qualquer caso de inconsistência) e o 
dinheiro deve voltar para a carteira do usuário que envia;

9. [ ] No recebimento de pagamento, o usuário ou lojista precisa receber notificação (envio de email, sms) enviada por um 
serviço de terceiro e eventualmente este serviço pode estar indisponível/instável. Use este mock 
https://util.devi.tools/api/v1/notify)) para simular o envio da notificação utilizando o verbo POST;

10. [ ] Este serviço deve ser RESTFul.

# Endpoint de transferência

Você pode implementar o que achar conveniente, porém vamos nos atentar somente ao fluxo de transferência entre dois 
usuários. A implementação deve seguir o contrato abaixo.

```http request
POST /transfer
Content-Type: application/json

{
"value": 100.0,
"payer": 4,
"payee": 15
}
```

❗Inserir outros endpoints também.

# Boas práticas

1. [ ] Conhecimentos sobre REST
2. [ ] Uso do Git
3. [ ] Capacidade analítica
4. [ ] Apresentação de código limpo e organizado

Conhecimentos intermediários de construção de projetos manuteníveis:

1. [ ] Aderência a recomendações de implementação como as PSRs
2. [ ] Aplicação e conhecimentos de SOLID
3. [ ] Identificação e aplicação de Design Patterns
4. [ ] Noções de funcionamento e uso de Cache
5. [ ] Conhecimentos sobre conceitos de containers (Docker, Podman etc)
6. [ ] Documentação e descrição de funcionalidades e manuseio do projeto
7. [ ] Implementação e conhecimentos sobre testes de unidade e integração
8. [ ] Identificar e propor melhorias
9. [ ] Boas noções de bancos de dados relacionais


Aptidões para criar e manter aplicações de alta qualidade:

1. [ ] Aplicação de conhecimentos de observabilidade
2. [ ] Utlização de CI para rodar testes e análises estáticas
3. [ ] Conhecimentos sobre bancos de dados não-relacionais
4. [ ] Aplicação de arquiteturas (CQRS, Event-sourcing, Microsserviços, Monolito modular)
5. [ ] Uso e implementação de mensageria
6. [ ] Noções de escalabilidade
7. [ ] Boas habilidades na aplicação do conhecimento do negócio no software
8. [ ] Implementação margeada por ferramentas de qualidade (análise estática, PHPMD, PHPStan, PHP-CS-Fixer etc)
9. [ ] Noções de PHP assíncrono

# Estrutura pacote

```text
com.moreira.picpaychallenge
├── application        # Regras de negócio específicas de casos de uso
│   ├── dto            # Objetos de transferência de dados
│   ├── services       # Implementações dos casos de uso
│   └── mappers        # Conversores entre entidades e DTOs
├── domain             # Camada de domínio (regra de negócio essencial)
│   ├── entities       # Entidades (modelos de negócio)
│   ├── enums          # Enumerações utilizadas pelo domínio
│   ├── exceptions     # Exceções de domínio
│   └── repositories   # Interfaces para acesso a dados
├── infrastructure     # Camada de infraestrutura (detalhes técnicos)
│   ├── database       # Implementações de repositórios (ex: JPA, MongoDB)
│   ├── configuration  # Configurações (Beans, Security, etc.)
│   └── clients        # Integrações com APIs externas
└── presentation       # Camada de apresentação (entrada/saída)
    ├── controllers    # Endpoints REST
    ├── handlers       # Tratamento de erros e exceções
    └── responses      # Objetos de resposta específicos
```

# Lógica de camada de negócio (Service)

Manter uma clean architecture utilizando o modelo supracitado.

Não é interessante termos uma classe de serviço para transferência ter acesso ao UserRepository.

Portanto, é bacana criarmos um UserService responsável pela lógica inicial:

4. [ ] Usuários podem enviar dinheiro (transferência) para lojistas e entre usuários;

5. [ ] Lojistas só recebem transferências, não enviam dinheiro para ninguém;

6. [ ] Validar se o usuário tem saldo antes da transferência;

Além disso, o UserService pode também possuir dois métodos (para encontrar Lojista/Usuário por id) e também um método
responsável por salvar os mesmos.

Assim, podemos ter uma classe de serviço para Transferência contendo a lógica de negócio utilizando os métodos do UserService.

## TransactionService

Inicialmente, a requisição que iremos receber é essa:

```http request
POST /transfer
Content-Type: application/json

{
"value": 100.0,
"payer": 4,
"payee": 15
}
```

Criaremos, portanto, um record (TransactionalDTO) recebendo tais dados da requisição.

## Como consultar serviço externo para finalizar transferência?

7. [ ] Antes de finalizar a transferência, deve-se consultar um serviço autorizador externo, use este mock
https://util.devi.tools/api/v2/authorize para simular o serviço utilizando o verbo GET;

Criaremos uma classe auxiliar chamada "TransferAuthorizationService". Ela será do tipo Service e utilizaremos o RestTemplate
para nos comunicarmos com essa url.

Inicialmente, iremos definir a url acima no properties como uma variável.

Esse link pode nos devolver:

- status "success" com auth true

```json
{
  "status": "success",
  "data": {
    "authorization": true
  }
}
```

- status "fail" com auth false

```json
{
  "status": "fail",
  "data": {
    "authorization": false
  }
}
```

O método ficará assim:

```java
import com.fasterxml.jackson.databind.JsonNode;
import com.moreira.picpaychallenge.domain.entities.User;

@Service
public class TransferAuthorizationService {

    private final RestTemplate restTemplate;

    @Value("${api.base.url}")
    private String apiBaseUrl;

    //Para o RestTemplate ser injetado por construtor, foi declarado em "AppConfig"
    public TransferAuthorizationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean authorizeTransfer(User user, BigDecimal amount) {
        //url para consultar passando parâmetro
        String url = apiBaseUrl + "/api/v2/authorize;

        try {
            //realizando chamada GET para api externa com resttemplate
            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, null, JsonNode.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode rootNode = response.getBody();
                JsonNode statusNode = rootNode.path("status"); //.path ou .get

                //navega pela estrutura do JSON da api até o campo de authorization
                JsonNode authorizationNode = rootNode.path("data").path("authorization");

                // Converte o valor de authorization para booleano. Se não existir, retorna 'false'
                if ("success".equals(statusNode.asText()) && authorizationNode.asBoolean()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                System.err.println("Falha na requisição de autorização: " + response.getStatusCode());
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error authorizing the transfer: " + e.getMessage());
            return false;
        }
    }
}
```

## Enviando notificação após transação (email + mock)

9. [ ] No recebimento de pagamento, o usuário ou lojista precisa receber notificação (envio de email, sms) enviada por um
serviço de terceiro e eventualmente este serviço pode estar indisponível/instável. Use este mock
https://util.devi.tools/api/v1/notify)) para simular o envio da notificação utilizando o verbo POST;


Iremos ter dois métodos: um para enviar email (usando provedor) e outro para enviar a requisição para o mock (resttemplate).

### Enviando notificação por email

Criaremos uma classe ``NotificationService``.

Importaremos o JavaMailSender e injetaremos no construtor.

Iremos declarar algumas variáveis no ``application.properties``.

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=seuemail@gmail.com
spring.mail.password=suasenha
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

Criaremos uma variável String de email retirando o "username" de properties.

Teremos um método void principal. Ele será responsável por disparar ambas notificações (por email e para o link acima).

Receberá como parâmetro um ``sender, receiver, value``.

```java
    public void sendTransferNotification(User sender, User receiver, BigDecimal amount) {
        //enviar email real para o remetente e destinatário
        sendNotificationEmail(sender, receiver, amount);

        //enviando para o mock de notificação
        sendMockNotification(sender, receiver, amount);
    }
```

#### Método 

O método de notificação será bem simples. Instanciaremos o email a ser enviado para o ``sender e para o receiver``.

```java
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
```


### Enviando notificação para o Mock (url)

Este método ficou um pouco mais longo, visto que precisamos criar um Mapa com Key(String) e value de outro Object.

Como pode observar, no campo Data, nós criamos outro map que irá conter a mensagem a ser passada.

```java
    private void sendMockNotification(User sender, User receiver, BigDecimal amount) {
        String url = apiBaseUrl + "/api/v1/notify";

        //criando corpo da requisição
        //inicialmente será Map e será convertido para JSON pelo RestTemplate
                                           //"Map.of" cria um mapa imutável
        Map<String, Object> notificationData = Map.of(
                //key     //value
                "status", "success",
                //key    value (que no caso, é outro mapa, que irá definir os detalhes da notificação)
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
```

Isso ⬆️, que antes era assim:

```json
{
  "status": "fail",
  "data": {
    "message": "Route 'GET:/api/v1/notify' not found"
  }
}
```

Se transformará nisso ⬇️

```json
{
  "status": "success",
  "data": {
    "message": "Transferência de R$ 500.00 de João Silva para Maria Oliveira concluída com sucesso."
  }
}
```


# Endpoint

