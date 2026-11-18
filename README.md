# Biblioteca TDD

Projeto da disciplina **Teste de Software**: atividade prática de Test-Driven Development (TDD).

Sistema de gerenciamento de empréstimos de uma biblioteca acadêmica, desenvolvido seguindo o ciclo **Red → Green → Refactor**.

## Contexto

Usuários (alunos e professores) pegam livros emprestados respeitando um limite de empréstimos simultâneos e um prazo de devolução, que variam conforme o tipo de usuário. Devoluções em atraso geram multa diária com valor máximo, e usuários com multa pendente ficam impedidos de fazer novos empréstimos até quitá-la. Um empréstimo pode ser renovado uma única vez, desde que não esteja atrasado.

| Classe | Responsabilidade |
|---|---|
| `Livro` | Título do acervo; controla os exemplares disponíveis. |
| `Usuario` | Aluno ou professor; controla empréstimos ativos e multa pendente. |
| `Emprestimo` | Retirada de um livro: prazo, atraso, renovação e devolução. |
| `CalculadoraMulta` | Calcula a multa por atraso (R$ 2,00/dia, teto de R$ 50,00). |

### Regras de negócio

| Código | Regra |
|---|---|
| RN01 | Livro sem exemplar disponível não pode ser emprestado. |
| RN02 | Aluno pode ter até 3 empréstimos simultâneos; professor, até 5. |
| RN03 | Usuário com multa pendente não pode fazer novo empréstimo. |
| RN04 | Prazo de devolução: 7 dias (aluno) e 14 dias (professor). |
| RN05 | Multa de R$ 2,00 por dia de atraso, com teto de R$ 50,00. |
| RN06 | Empréstimo só pode ser renovado uma vez e se não estiver atrasado. |

Detalhes completos em [`docs/requisitos.md`](docs/requisitos.md).

## Integrantes

- Jean Torres
- Danyllo Mendanha

## Tecnologias

- Java 17
- JUnit 5 (Jupiter)
- Maven
- JaCoCo (cobertura de testes)

## Como executar os testes

Na pasta do projeto (onde está o `pom.xml`):

```bash
mvn test
```

O relatório de cobertura é gerado em `target/site/jacoco/index.html`.

## Estrutura de pastas

```
biblioteca-tdd/
├── docs/
│   ├── requisitos.md               # Etapa 1: levantamento de requisitos
│   └── analise-beneficios.md       # Etapa 4: análise reflexiva sobre o TDD
├── src/
│   ├── main/java/br/com/biblioteca/
│   │   ├── CalculadoraMulta.java   # RN05: valor da multa
│   │   ├── Livro.java              # RN01: exemplares disponíveis
│   │   ├── Usuario.java            # RN02, RN03: limite e multa
│   │   ├── TipoUsuario.java        # Limite e prazo de cada tipo (RN02, RN04)
│   │   ├── Emprestimo.java         # RN04, RN05, RN06: prazo, devolução, renovação
│   │   └── *Exception.java         # Exceções de negócio
│   └── test/java/br/com/biblioteca/
│       ├── CalculadoraMultaTest.java
│       ├── LivroTest.java
│       ├── UsuarioTest.java
│       └── EmprestimoTest.java
├── pom.xml                         # Maven + JUnit 5 + JaCoCo
└── README.md
```

## Padrão de commits

Cada ciclo TDD gera commits separados, o que deixa o ciclo visível no histórico (`git log --oneline`):

| Fase      | Prefixo     | Exemplo                                                          |
|-----------|-------------|------------------------------------------------------------------|
| Red       | `test:`     | `test: Livro sem exemplar nao pode ser emprestado - RN01 (red)`  |
| Green     | `feat:`     | `feat: bloqueia emprestimo de livro sem exemplar - RN01 (green)` |
| Green (correção de bug) | `fix:` | `fix: guarda multa em BigDecimal para quitar valores com centavos (green)` |
| Refactor  | `refactor:` | `refactor: move limite e prazo para TipoUsuario, removendo condicionais (OCP)` |

## Resultados

| Métrica | Valor |
|---|---|
| Testes | **47** (todos passando) |
| Cobertura de linhas | **96%** (109 de 113; só ficaram de fora getters simples) |
| Cobertura de branches | **100%** (50 de 50) |
| Ciclos Red → Green | 28 |
| Refatorações | 4 |
| Bugs encontrados pelos testes | 3 (ver [`docs/analise-beneficios.md`](docs/analise-beneficios.md)) |

| Classe de teste | Testes |
|---|---|
| `CalculadoraMultaTest` | 5 |
| `LivroTest` | 9 |
| `UsuarioTest` | 16 |
| `EmprestimoTest` | 17 |
