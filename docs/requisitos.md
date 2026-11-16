# Levantamento de Requisitos — Biblioteca TDD

## 1. Contexto

O sistema gerencia os empréstimos de livros de uma biblioteca acadêmica. Usuários (alunos e professores) podem pegar livros emprestados respeitando um limite de empréstimos simultâneos e um prazo de devolução, que variam conforme o tipo de usuário. Devoluções em atraso geram multa diária com valor máximo, e usuários com multa pendente ficam impedidos de fazer novos empréstimos até quitá-la. Um empréstimo pode ser renovado uma única vez, desde que não esteja atrasado.

## 2. Classes e responsabilidades

| Classe | Responsabilidade |
|---|---|
| `Livro` | Representa um título do acervo e controla a quantidade de exemplares disponíveis. |
| `Usuario` | Representa aluno ou professor; controla limite de empréstimos e multas pendentes. |
| `Emprestimo` | Registra a retirada de um livro por um usuário, prazos, atraso, devolução e renovação. |
| `CalculadoraMulta` | Calcula o valor da multa por atraso aplicando as regras de cobrança. |

Apoio: enum `TipoUsuario` (`ALUNO`, `PROFESSOR`) e as exceções `LivroIndisponivelException`, `UsuarioBloqueadoException`, `RenovacaoNaoPermitidaException`.

## 3. Métodos (assinatura e comportamento esperado)

### Livro — `new Livro(String isbn, String titulo, int quantidadeExemplares)`
| # | Método | Comportamento esperado |
|---|---|---|
| 1 | `boolean estaDisponivel()` | Retorna `true` se houver ao menos 1 exemplar disponível. |
| 2 | `int getExemplaresDisponiveis()` | Retorna a quantidade de exemplares disponíveis. |
| 3 | `void emprestarExemplar()` | Diminui 1 exemplar disponível. Lança `LivroIndisponivelException` se não houver exemplar. |
| 4 | `void devolverExemplar()` | Aumenta 1 exemplar disponível. Lança `IllegalStateException` se todos já estiverem disponíveis. |

Construtor: lança `IllegalArgumentException` se ISBN for vazio ou quantidade ≤ 0.

### Usuario — `new Usuario(String nome, TipoUsuario tipo)`
| # | Método | Comportamento esperado |
|---|---|---|
| 5 | `int getLimiteEmprestimos()` | Aluno: 3. Professor: 5. |
| 6 | `int getPrazoDevolucaoEmDias()` | Aluno: 7 dias. Professor: 14 dias. |
| 7 | `boolean podeEmprestar()` | `false` se atingiu o limite ou tem multa pendente. |
| 8 | `void registrarEmprestimo()` | Soma 1 empréstimo ativo. Lança `UsuarioBloqueadoException` se `podeEmprestar()` for `false`. |
| 9 | `void registrarDevolucao()` | Subtrai 1 empréstimo ativo. Lança `IllegalStateException` se não houver empréstimo ativo. |
| 10 | `void adicionarMulta(double valor)` | Soma valor à multa pendente. Lança `IllegalArgumentException` se valor ≤ 0. |
| 11 | `void pagarMulta(double valor)` | Abate o valor da multa pendente. Lança `IllegalArgumentException` se valor ≤ 0 ou maior que a multa. |

### Emprestimo — `new Emprestimo(Livro livro, Usuario usuario, LocalDate dataEmprestimo)`
| # | Método | Comportamento esperado |
|---|---|---|
| 12 | `LocalDate getDataPrevistaDevolucao()` | Data do empréstimo + prazo do tipo de usuário. |
| 13 | `boolean estaAtrasado(LocalDate hoje)` | `true` se `hoje` for depois da data prevista e não tiver sido devolvido. |
| 14 | `int getDiasAtraso(LocalDate data)` | Dias entre a data prevista e a data informada; 0 se não atrasado. |
| 15 | `void renovar(LocalDate hoje)` | Estende o prazo pelo mesmo número de dias. Lança `RenovacaoNaoPermitidaException` se já renovado ou atrasado. |
| 16 | `void devolver(LocalDate dataDevolucao)` | Marca como devolvido, devolve o exemplar e gera multa no usuário se houver atraso. Lança `IllegalStateException` se já devolvido e `IllegalArgumentException` se a data for anterior ao empréstimo. |

### CalculadoraMulta
| # | Método | Comportamento esperado |
|---|---|---|
| 17 | `double calcular(int diasAtraso)` | R$ 2,00 por dia, limitado a R$ 50,00. 0 dias → R$ 0,00. Lança `IllegalArgumentException` se dias < 0. |

**Total: 4 classes de domínio, 17 métodos testáveis.**

## 4. Regras de negócio

| Código | Regra | Validada em |
|---|---|---|
| RN01 | Livro sem exemplar disponível não pode ser emprestado. | `Livro.emprestarExemplar` |
| RN02 | Aluno pode ter até 3 empréstimos simultâneos; professor, até 5. | `Usuario.podeEmprestar` |
| RN03 | Usuário com multa pendente não pode fazer novo empréstimo. | `Usuario.podeEmprestar` |
| RN04 | Prazo de devolução: 7 dias (aluno) e 14 dias (professor). | `Emprestimo.getDataPrevistaDevolucao` |
| RN05 | Multa de R$ 2,00 por dia de atraso, com teto de R$ 50,00. | `CalculadoraMulta.calcular` |
| RN06 | Empréstimo só pode ser renovado uma vez e se não estiver atrasado. | `Emprestimo.renovar` |

## 5. Cenários de exceção por classe

| Classe | Cenário | Exceção |
|---|---|---|
| `Livro` | Emprestar livro sem exemplar disponível | `LivroIndisponivelException` |
| `Usuario` | Registrar empréstimo com multa pendente ou limite atingido | `UsuarioBloqueadoException` |
| `Emprestimo` | Renovar empréstimo atrasado ou já renovado | `RenovacaoNaoPermitidaException` |
| `CalculadoraMulta` | Calcular multa com dias negativos | `IllegalArgumentException` |

## 6. Ordem planejada dos ciclos TDD

1. `CalculadoraMulta` (sem dependências — bom para começar)
2. `Livro`
3. `Usuario`
4. `Emprestimo` (depende das anteriores)

Em cada classe: caminho feliz primeiro, depois casos de borda e exceções.
