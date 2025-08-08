### O método escolhido para interação com o sistema
Eu escolhi o metodo REST para a interação com o sistema, pois como a operação de analise de risco é sincrona, esse método consegue entregar o score com um bom tempo de resposta, com uma baixa complexidade. Gostaria de ter utilizado algum protocolo mais moderno como gRPC, mas como o tempo era curto, optei por algo mais simples e que funcionasse bem.

### Como executar o sistema (ex: comandos para subir as aplicações).
Para iniciar é necessario ter o docker e o docker-compose instalado. Feito isso é só rodar:
```bash
docker-compose build
docker-compose up
```

### Como testar o sistema (ex: endpoints, dados de exemplo, ferramentas recomendadas).
Recomendo utilizar o Bruno (http client) para fazer as requisições. Só abrir a collection (bruno-collection/analise-risco)

# Analise Risco API (Porta 8190)

This Spring Boot application provides a REST endpoint to perform risk analysis. A scoring decision is returned based on the input data.

## Endpoints

### POST /api/analise_risco/

- **Description:**  
  Calcula a decisão do score (e.g., APROVADO, NEGADO, INVALID_RESPONSE, or ERROR).
### JSON Payload Example

```json
{
  "ip": "192.168.1.1",
  "cpf": "123.456.789-10",
  "device_id": "device123",
  "tx_type": "PIX",
  "tx_value": "100.00"
}
```
### Exemplo Resposta
```json
{
  "txDecision": "APROVADO"
}
```

--- 
# Listas Permissoes Restricões API (Porta 8191)

## Endpoints

### POST /api/rule/allow

- **Description:**  
  Adiciona um IP, CPF, device_id, a uma lista de permissões.
- 
### JSON Payload Example

```json
{
  "cpf": "422.111.111-22",
  "deviceId": null,
  "ip": null
}
```

### POST /api/rule/deny

- **Description:**  
  Adiciona um IP, CPF, device_id, a uma lista de restrições.
-
#### JSON Payload Example

```json
{
  "cpf": "422.111.111-22",
  "deviceId": null,
  "ip": null
}
```

### POST /api/rule/validate

- **Description:**  
  Verifica se a transação tem campos permitidos ou restritos.
-
#### JSON Payload Example

```json
{
  "cpf": "422.111.111-22",
  "deviceId": "12312",
  "ip": "192.168.15.1"
}
```
#### Exemplo Resposta
```json
{
  "cpf": "422.111.111-22",
  "ip": "192.168.15.1",
  "denyFields": ["cpf", "ip"],
  "allowFields": ["device_id"],
  "device_id": "12312"
}
```
# Motor Score Api (Porta 8192)

## Endpoints

### POST /api/score/calculate

- **Description:**  
  - Faz o calculo do score baseado nos dados de entrada.
### JSON Payload Example

```json
{
  "cpf": "422.111.111-22",
  "ip": "192.168.15.1",
  "denyFields": [
    "ip",
    "device_id",
    "cpf"
  ],
  "allowFields": [
    "cpf"
  ],
  "device_id": "12312",
  "tx_type": "PIX",
  "tx_value": "100"
}
```
#### Exemplo Resposta
```json
{
  "cpf": "422.111.111-22",
  "ip": "192.168.15.1",
  "denyFields": [
    "ip",
    "device_id",
    "cpf"
  ],
  "allowFields": [
    "cpf"
  ],
  "device_id": "12312",
  "tx_type": "PIX",
  "tx_value": "100",
  "tx_score": 1200
}
```
### POST /api/score/

- **Description:**  
  - Faz o cadastro de uma regra de score.
  - A estrutura de dados foi baseada na criação de rotinas da Alexa e no IFTTT (If This Then That), funciona da seguinte forma:
    Uma regra é formada apenas de condições e ações. As condições são avaliadas e se todas forem verdadeiras, as ações são executadas. Isso permite maior flexibilidade na criação de regras, sem deixar complexo demais.
  - Condições suportadas: 
    - EQUALS
    - GREATHER_THAN
    - LESS_THAN
    - GREATHER_THAN_OR_EQUALS
    - LESS_THAN_OR_EQUALS
    - BETWEEN
  - Ações suportadas:
    - ADD
    - SUBTRACT

### JSON Payload Example

```json
{
  "name": "Uma regra",
  "conditions": [{
      "field": "tx_type",
      "conditionType": "EQUALS",
      "value": "PIX"
    },
    {
      "field": "tx_value",
      "conditionType": "GREATHER_THAN",
      "value": "100.0"
    }
  ],
  "actions": [{
    "field": "tx_score",
    "actionType": "ADD",
    "value": "200000.0"
  }]
}

```

### Qualquer outra informação relevante que facilite o entendimento e uso do projeto
### Detalhes sobre a solução, gostaríamos de saber qual foi o seu racional nas decisões.
### Caso algo não esteja claro e você precisou assumir alguma premissa quais forame o que te motivou a tomar essas decisões.