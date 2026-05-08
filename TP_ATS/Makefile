.DEFAULT_GOAL := help

MVN := mvn

# ─────────────────────────────────────────────────────────────
# HELP
# ─────────────────────────────────────────────────────────────
.PHONY: help
help: ## Mostra esta mensagem de ajuda
	@echo ""
	@echo "  SpotifUM — Análise e Teste de Software"
	@echo ""
	@echo "  Targets disponíveis:"
	@echo ""
	@awk 'BEGIN {FS = ":.*##"} /^[a-zA-Z0-9_-]+:.*##/ { printf "    \033[36m%-22s\033[0m %s\n", $$1, $$2 }' $(MAKEFILE_LIST)
	@echo ""
	@echo "  Exemplo de uso rápido:  make run"
	@echo ""

# ─────────────────────────────────────────────────────────────
# TARGETS INDIVIDUAIS
# ─────────────────────────────────────────────────────────────
.PHONY: clean
clean: ## Limpa os artefactos de build anteriores
	@echo "\n🧹 A limpar o projeto...\n"
	@$(MVN) clean -q

.PHONY: compile
compile: ## Compila o código fonte
	@echo "\n🔨 A compilar o projeto...\n"
	@$(MVN) compile -q

.PHONY: test
test: ## Corre os testes unitários (JUnit 5 + jqwik) e gera relatório JaCoCo
	@echo "\n🧪 A correr os testes unitários e a gerar o relatório JaCoCo...\n"
	@$(MVN) test jacoco:report
	@echo "\n✅ Relatório JaCoCo disponível em: target/site/jacoco/index.html\n"

.PHONY: evosuite
evosuite: ## Corre os testes unitários + testes EvoSuite e gera relatório JaCoCo combinado
	@echo "\n🤖 A correr testes EvoSuite + unitários com JaCoCo...\n"
	@$(MVN) test jacoco:report -P evosuite
	@echo "\n✅ Relatório JaCoCo (EvoSuite) disponível em: target-evosuite/site/jacoco/index.html\n"

.PHONY: jacoco-merge
jacoco-merge: ## Faz merge dos relatórios JaCoCo (unitários + EvoSuite) num só
	@echo "\n🔀 A fazer merge dos relatórios JaCoCo...\n"
	@$(MVN) verify -P jacoco-merge -DskipTests
	@echo "\n✅ Relatório JaCoCo combinado disponível em: target/site/jacoco-merged/index.html\n"

.PHONY: pit
pit: ## Corre o PIT mutation testing e gera relatório HTML
	@echo "\n☠️  A correr PIT mutation testing...\n"
	@$(MVN) org.pitest:pitest-maven:mutationCoverage
	@echo "\n✅ Relatório PIT disponível em: target/pit-reports/index.html\n"

# ─────────────────────────────────────────────────────────────
# TARGET PRINCIPAL
# ─────────────────────────────────────────────────────────────
.PHONY: run
run: clean compile test evosuite jacoco-merge pit ## ▶ Executa o pipeline completo (recomendado)
	@echo ""
	@echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
	@echo "  ✅  Pipeline completo concluído com sucesso!"
	@echo ""
	@echo "  Relatórios gerados:"
	@echo "    • JaCoCo (testes unitários) → target/site/jacoco/index.html"
	@echo "    • JaCoCo (EvoSuite)         → target-evosuite/site/jacoco/index.html"
	@echo "    • JaCoCo (combinado)        → target/site/jacoco-merged/index.html"
	@echo "    • PIT mutation testing      → target/pit-reports/index.html"
	@echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
	@echo ""