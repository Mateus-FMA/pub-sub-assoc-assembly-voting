# Documentação técnica

Este documento detalha as tecnologias utilizadas e o motivo por trás da escolha de cada uma delas,
além de descrever possíveis melhorias.

## Ferramentas utilizadas

- *Spring WebFlux*, framework para implementação de APIs REST. Utiliza o paradigma de programação
reativo, o que facilita escalar utilizando menos recursos de hardware, além de lidar com concorrência
com um baixo número de _threads_. Em particular, a escolha da arquitetura reativa facilitou a
implementação das sessões de votação.

- *PostgreSQL + Spring Data R2DBC* para utilizar a camada de dados relacional com uma abordagem não
bloqueante de E/S (_non-blocking I/O_). Muito embora a escolha do banco se deva pela maior
familiaridade que eu tenha com a tecnologia, a escalabilidade da base de dados para bancos relacionais
é vertical e, portanto, mais custosa. Uma alternativa seria utilizar algum banco NoSQL (e.g. MongoDB).
