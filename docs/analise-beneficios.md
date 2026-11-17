# Análise dos Benefícios do TDD — Biblioteca TDD

## 1. Evolução do código: como o design emergiu dos testes

A implementação seguiu a ordem planejada nos requisitos: `CalculadoraMulta` → `Livro` → `Usuario` → `Emprestimo`, começando pela classe sem dependências. Ao todo o histórico tem **28 ciclos Red → Green** e **4 refatorações**.

Alguns momentos em que o design mudou por causa dos testes:

- **`CalculadoraMulta` cresceu em passos pequenos.** O primeiro Green devolvia `0.0` fixo. O teste de 5 dias forçou `dias * 2.0`, o teste de 30 dias forçou o `Math.min` do teto, e o teste com dias negativos trouxe a validação. Só depois, com tudo verde, as constantes `VALOR_POR_DIA` e `VALOR_MAXIMO` foram extraídas (refactor).
- **As regras por tipo de usuário saíram do `Usuario` e foram para o enum `TipoUsuario`.** No Green, limite e prazo eram `tipo == ALUNO ? 3 : 5` e `? 7 : 14`. Com os testes de RN02 e RN04 servindo de rede de segurança, a refatoração moveu esses valores para o próprio enum (`ALUNO(3, 7)`, `PROFESSOR(5, 14)`). Assim um novo tipo de usuário (ex.: servidor) é adicionado sem alterar `Usuario`, o que é o **princípio Aberto/Fechado (OCP)**.
- **`Emprestimo` passou a receber a `CalculadoraMulta` por construtor.** No Green, `devolver` criava `new CalculadoraMulta()` direto. Na refatoração foi criado um segundo construtor que recebe a calculadora (o construtor original continua usando a padrão), aplicando a **Inversão de Dependência (DIP)**: outra política de multa pode ser trocada sem mexer em `Emprestimo`.
- **Responsabilidade única (SRP):** cada classe ficou com uma responsabilidade clara (estoque do livro, situação do usuário, ciclo de vida do empréstimo e cálculo da multa), porque cada arquivo de teste exercitava um único conceito.

## 2. Bugs encontrados pelos testes antes da integração

Os três bugs abaixo aparecem no histórico de commits como um teste Red seguido de um commit `fix:` Green.

| # | Bug | Teste que pegou | Correção | Commits |
|---|---|---|---|---|
| 1 | **Resíduo de arredondamento com `double`.** Adicionar multas de R$ 0,10 e R$ 0,20 e pagar R$ 0,30 deixava `5.55E-17` de multa pendente, e o usuário continuava **bloqueado** mesmo depois de quitar. | `UsuarioTest.deveQuitarMultaQuandoValoresTiveremCentavos` | Multa guardada em `BigDecimal` com 2 casas. | `3fe8b8c` → `c5a105c` |
| 2 | **Estado inconsistente ao recusar empréstimo.** O `Emprestimo` registrava o empréstimo no usuário e só depois tentava pegar o exemplar. Com o livro esgotado, a exceção era lançada, mas o usuário já ficava com 1 empréstimo ativo "fantasma". | `EmprestimoTest.naoDeveAlterarUsuarioQuandoLivroEstiverIndisponivel` | Validar o usuário antes de qualquer alteração e só então alterar livro e usuário. | `671b186` → `4b07927` |
| 3 | **Regressão: multa por atraso zerada.** Ao implementar "empréstimo devolvido não está atrasado", `estaAtrasado` passou a olhar o estado `devolvido`. Como `getDiasAtraso` reaproveitava `estaAtrasado` e `devolver` marca o empréstimo como devolvido antes de calcular a multa, toda devolução atrasada passou a gerar multa **R$ 0,00**. | Os testes **já existentes** de RN05 (`deveGerarMultaQuandoDevolverComAtraso` e `deveLimitarMultaQuandoAtrasoForMuitoLongo`) quebraram na hora. | `getDiasAtraso` passou a comparar só as datas. | `0f72176` → `c227920` |

O bug 3 mostra bem a "rede de segurança": a mudança parecia inofensiva, mas quebrou uma regra de negócio em outro método, e a suíte avisou na hora, antes de o código chegar a qualquer usuário.

## 3. Comparativo: sem TDD × com TDD

| Aspecto | Sem TDD (código primeiro, teste depois) | Com TDD (neste projeto) |
|---|---|---|
| Bugs de arredondamento e de estado inconsistente | Provavelmente só apareceriam em uso real: um aluno que pagou a multa e continua bloqueado, ou um contador de empréstimos errado. | Encontrados durante o desenvolvimento, cada um com um teste que impede a volta do bug. |
| Regressões | A regressão da multa zerada passaria despercebida, porque o método alterado era outro. | Detectada na mesma hora pelos testes antigos. |
| Cobertura | Depende de disciplina no fim do projeto, quando costuma faltar tempo. | 47 testes, **96% das linhas e 100% dos branches** cobertos, por construção. |
| Design | Tendência a condicionais por tipo espalhadas e dependências criadas com `new` dentro dos métodos. | Refatorações seguras levaram a OCP (`TipoUsuario`) e DIP (`CalculadoraMulta` injetada). |
| Documentação | Requisitos ficam só no documento e podem ficar desatualizados. | Cada teste tem `@DisplayName` e cita a regra (RN01–RN06): os testes descrevem o comportamento esperado. |
| Custo | Menos tempo no início, mais tempo depurando depois. | Mais commits e mais tempo no início, compensados por depurar quase nada. |

## 4. Lições aprendidas

1. **Passos pequenos de verdade funcionam.** Implementar o mínimo (até devolver `0.0` fixo) parece exagero, mas obriga cada comportamento a ter um teste próprio.
2. **Ver o teste falhar importa.** Em todos os ciclos o teste foi executado e falhou antes da implementação. Isso garante que o teste realmente verifica algo e não passa "por acaso".
3. **Dinheiro não combina com `double`.** O bug de centavos só foi encontrado porque o teste usou valores com casas decimais. Casos de borda com centavos devem fazer parte de qualquer suíte que lida com valores monetários.
4. **Operações que alteram vários objetos precisam validar tudo antes de alterar.** O bug do empréstimo "fantasma" ensinou a ordem: validar primeiro, alterar depois.
5. **A suíte de testes é o que permite refatorar sem medo.** As refatorações para OCP e DIP mexeram em várias classes, e mesmo assim foram feitas com confiança porque todos os testes continuaram verdes.
6. **Um commit por fase deixa o histórico contar a história.** É possível mostrar exatamente quando cada regra nasceu e quando cada bug foi descoberto e corrigido.
