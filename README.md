# Biblioteca TDD

Projeto da disciplina **Teste de Software** — atividade prática de Test-Driven Development (TDD).

Sistema de gerenciamento de empréstimos de uma biblioteca, desenvolvido seguindo o ciclo **Red → Green → Refactor**.

## Integrantes

- Nome do integrante 1
- Nome do integrante 2

## Tecnologias

- Java 17
- JUnit 5 (Jupiter)
- Maven
- JaCoCo (cobertura de testes)

## Como executar os testes

```bash
mvn test
```

Relatório de cobertura gerado em `target/site/jacoco/index.html`.

## Estrutura de pastas

```
biblioteca-tdd/
├── docs/
│   └── requisitos.md          # Levantamento de requisitos (Etapa 1)
├── src/
│   ├── main/java/br/com/biblioteca/   # Código de produção
│   └── test/java/br/com/biblioteca/   # Testes (escritos ANTES do código)
├── pom.xml                    # Configuração Maven + JUnit 5 + JaCoCo
└── README.md
```

## Padrão de commits

| Fase      | Prefixo     | Exemplo                                              |
|-----------|-------------|------------------------------------------------------|
| Red       | `test:`     | `test: Livro deve emprestar exemplar disponível (red)` |
| Green     | `feat:`     | `feat: implementa Livro.emprestarExemplar (green)`   |
| Refactor  | `refactor:` | `refactor: extrai validação de quantidade em Livro`  |

## Resultados

_Em andamento._
