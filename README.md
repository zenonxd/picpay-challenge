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