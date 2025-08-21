### Desafio Especialista Backend - Análise de Risco
### Nome: Kevim

### Pagina do desafio
- https://github.com/dofun12/desafio_especialista.git

### O método escolhido para interação com o sistema
Eu escolhi o metodo REST para a interação com o sistema, pois como a operação de analise de risco é sincrona, esse método consegue entregar o score com um bom tempo de resposta, com uma baixa complexidade. Gostaria de ter utilizado algum protocolo mais moderno como gRPC, mas como o tempo era curto, optei por algo mais simples e que funcionasse bem.

### Como executar o sistema
Para iniciar é necessario ter o docker e o docker-compose instalado. Feito isso é só rodar:
```bash
docker-compose build
docker-compose up
```

### Como testar o sistema
Recomendo utilizar o Bruno (http client) para fazer as requisições. Só abrir a collection (bruno-collection/analise-risco).


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

### Informações relevantes

- Arquitetura do sistema:
![Diagrama de infra estrutura](images/analise_riscos.drawio.svg)

### Detalhes sobre a solução
  - A solução foi dividida em três microserviços:
  - Analise Risco API: Responsável por receber as requisições e fazer a validação dos dados.
  - Listas Permissoes Restricões API: Responsável por gerenciar as listas de permissões e restrições.
  - Motor Score API: Responsável por calcular o score baseado nas regras definidas.
  - Foi pensado em ter dois bancos de dados diferentes, um para Listas Permissoes Restricões e outro para o Motor Score, acredito que ambos terão usos diferentes do banco de dados, dessa forma é mais facil de fazer um auto scale eficiente.
  - Foi adicionado um cache em redis para melhorar a consulta de condições do Motor Score, assim evitando consultas desnecessárias ao banco de dados, para menos de um rps, não vai fazer muita diferença, mas para mais de 100 rps, já faz uma diferença significativa.
  - A estrutura de dados foi baseada na criação de rotinas da Alexa e no IFTTT (If This Then That), funciona da seguinte forma, uma regra é formada apenas de condições e ações. As condições são avaliadas e se todas forem verdadeiras, as ações são executadas. Isso permite maior flexibilidade na criação de regras, sem deixar complexo demais.
  - O prazo foi mais curto que o combinado, pois estava de férias quando foi enviado o email do desafio, apenas 4 dias, então não consegui implementar tudo que gostaria, como por exemplo:
    - Implementar autenticação e autorização.
    - Implementar testes automatizados.
    - Implementar uma interface gráfica para facilitar o uso do sistema.
    - Implementar um sistema de logs mais robusto.
    - Implementar um sistema de monitoramento e alerta.
  

## Gatling
O gatling foi utilizado para fazer os testes de carga do sistema, o arquivo de configuração está na pasta test-gatling. Para rodar os testes, basta executar o comando:
Ele é feito para rodar fora do docker, então é necessário ter o maven e o java instalado na máquina.
```bash
mvn clean install package && mvn gatling:test