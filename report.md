Inicio do projeto tinhamos 235 testes unitarios às varias classes, nesta fase apenas um teste falhava:

- **testHandleInputChangePlans**:
  - Expected: true
  - Actual: false
  - Explicação: bug do case 3 sem break
  - O switch-case cai para o case 4 por ausência de break, navegando para o PlanMenu

Durante a execução inicial da suite de testes foi detetado um problema de compatibilidade entre o Java 25 (Homebrew) e o Mockito 5.11.0. O Mockito utiliza inline mocking que requer modificação de bytecode em runtime (via dynamic attach), operação que o Java 25 passou a restringir por razões de segurança. A solução passou por configurar o Maven Surefire Plugin para carregar o agente Mockito explicitamente via -javaagent, garantindo compatibilidade sem alterar a versão do Java nem do Mockito. Esta configuração é necessária em qualquer ambiente com Java 21+.


**Testes na serialização**

Foram adicionados testes unitarios para o utilitario de serializacao, cobrindo o fluxo principal (round‑trip) e os cenarios de erro mais provaveis (null, ficheiro inexistente, caminho invalido).”
“O teste de round‑trip confirma que a serializacao preserva o estado observado por SpotifUM.equals.”
“Nota: SpotifUM.equals nao compara estatisticas (artistReproductions, genreReproductions), pelo que a cobertura total do estado depende dessa implementacao.