# 🌐 GOS — Global Order Service

API REST em Spring Boot que recebe pedidos de clientes em **qualquer moeda** (USD, EUR, etc.),
converte o valor para **Real (BRL)**, aplica uma **taxa de envio** (frete) conforme o tipo de
entrega e persiste o pedido em banco.

---

## 🎯 Objetivo - Projeto

O GOS centraliza a criação e consulta de pedidos globais, garantindo que:

- O valor original (em moeda estrangeira) seja **convertido para BRL** usando cotação em tempo real.
- Uma **taxa de frete** seja aplicada de acordo com o tipo de envio (`STANDARD`, `EXPRESS`, `VIP`).
- O pedido seja **salvo** e possa ser **consultado** posteriormente.

---

## 🎯 Objetivo - Acadêmico

- Praticar a criação de um projeto real utilizando Java 25 e Spring Boot. Neste projeto é utilizado o banco em memória H2.
- Utilizar design patterns de uma maneira prática, para melhor entendimento.
    - Creational Pattern
        - Factory
        - Builder
    - Structural Pattern
      - Adapter
    - Behavioral Pattern
        - Strategy

---

## 🏗️ Arquitetura

O projeto segue o padrão em camadas do Spring, onde cada camada conversa apenas com a imediatamente
inferior:

### 1. `controller` — porta de entrada
`OrderController` expõe os endpoints REST:

| Método | Endpoint           | Descrição            |
|--------|--------------------|----------------------|
| POST   | `/api/v1/orders`   | Cria um novo pedido  |
| GET    | `/api/v1/orders`   | Lista todos os pedidos |

Recebe o JSON via `OrderRequestDTO`, delega ao `OrderService` e devolve o `OrderResponseDTO`.
**Não contém regra de negócio.**

### 2. `service` — o cérebro
`OrderService.createOrder()` orquestra o fluxo:

1. Consulta a taxa de câmbio da moeda informada.
2. Converte o valor para BRL.
3. Usa a `FeeStrategyFactory` para selecionar a estratégia de frete correta.
4. Monta o `Order` com `OrderBuilder` (calcula `convertedAmount` e `finalAmount`).
5. Salva via `OrderRepository`.

### 3. `client` — integração externa
`AwesomeExchangeRateAdapterImpl` consome a API pública
[awesomeapi](https://economia.awesomeapi.com.br/last) para obter a cotação.
Implementa a interface `ExchangeRateAdapter`, então trocar de provedor de câmbio no futuro
exige apenas uma nova implementação — sem alterar o `Service`.

### 4. `repository` — persistência
`OrderRepository` estende `JpaRepository`, herdando salvar/buscar automaticamente.
O banco utilizado é o **H2 em memória** (`application.yaml`), ideal para testes — os dados
são perdidos quando a aplicação é encerrada.

### 5. `model` — entidades e dados
- `Order`: entidade JPA persistida na tabela `tb_order`.
- `ShippingTypeEnum`: `STANDARD`, `VIP`, `EXPRESS`.
- `OrderBuilder`: padrão *Builder* para construir o `Order` e calcular os totais.

---

## 💡 Padrão Strategy (taxas de frete)

As taxas de frete variam por tipo de envio. Para manter o código extensível, o projeto usa o
**padrão Strategy**:

- `FeeStrategy` (interface) → `calculateFee()` e `getShippingType()`.
- Implementações: `StandardFeeStrategy`, `ExpressFeeStrategy` (12%), `VipFeeStrategy`.
- `FeeStrategyFactory` recebe **todas** as estratégias e monta um mapa `tipo → estratégia`.

Adicionar um novo tipo de frete significa criar uma nova classe — **sem mexer no `Service`**.

---

## 🔄 Fluxo de um `POST /api/v1/orders`

```json
{
  "customerName": "Edudev",
  "currency": "USD",
  "amount": 100.00,
  "shippingType": "EXPRESS"
}