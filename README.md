<h2 align="center">NLW-Connect 🚀</h2>
<h5 align="center">Evento Rocketseat 2025 Edição Java</h5>

## 💻 Descrição
Nesse evento, será construída uma aplicação do zero em Java utilizando o framework Spring Boot junto com Docker e MySQL.
Aplicação com objetivo de gerenciar eventos e suas respectivas inscrições.

## 💻 Aula 01 - Construindo a base do projeto
Primeiros passos para a criação da API.
Criação do projeto pelo [SpringInitializr](https://start.spring.io/)], configurando conexão com banco de dados e demais arquivos necessários para iniciar o projeto.
Criação da estrutura basica de Model - View - Controller, com service para gerenciar as regras de negócios.
Iniciando com a criação da entidade Event.

## 💻 Aula 02 - Implementando o Gerenciador de Inscrição
Nessa aula foi implementada toda a lógica para gerenciar a inscrição e todas as regras de negócio envolvendo Usuário e Evento.
Respectivamente criamos a estrutura da tabela Subscription.
Seguindo para seu repository, service e controller onde foi necessário o tratamento de exceções e DTO's adicionais.

BÔNUS: Nessa aula algumas exceções foram tratadas da forma tradicional (if), porém, pensei em trata-las utilizando o Optional e .orElseThrow com objetivo de estudar novas formas e deixar o código mais legível :)


