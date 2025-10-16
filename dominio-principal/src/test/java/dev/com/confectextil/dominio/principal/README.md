# Testes do Módulo Fabrico

Este diretório contém os testes BDD (Behavior-Driven Development) para a funcionalidade de cadastro e edição de Fabrico usando Cucumber com JUnit 5.

## Estrutura de Domínio (DDD)

O módulo segue os princípios de Domain-Driven Design (DDD) conforme especificação CML:

### Aggregate Fabrico

**Entity Fabrico** (Aggregate Root)
- `FabricoId id` (final) - Value Object identificador
- `String nomeFantasia` - Nome fantasia da empresa
- `String cnpj` - CNPJ no formato XX.XXX.XXX/XXXX-XX

**ValueObject FabricoId**
- Encapsula o identificador único do Fabrico
- Implementado como Java Record
- Métodos factory: `novo(String)` e `comValor(String)`

**Repository FabricoRepository**
- `void salvar(Fabrico)` - Persiste um novo Fabrico
- `Fabrico editar(Fabrico)` - Atualiza um Fabrico existente
- `Optional<Fabrico> buscarPorId(FabricoId)` - Busca por ID

## Estrutura dos Testes

### Arquivos Principais

1. **FabricoStepDefinitions.java** - Implementa os steps definitions (Dado/Quando/Então) do Cucumber
2. **FabricoTestRunner.java** - Classe runner do JUnit que executa os testes Cucumber
3. **fabrico.feature** - Arquivo de especificação em Gherkin (localizado em `src/test/resources/dev/com/confectextil/dominio/principal/teste/`)

### Cenários de Teste

Os testes cobrem os seguintes cenários:

1. ✅ **Cadastrar Fabrico com sucesso** - Valida cadastro com dados válidos
2. ✅ **Falha ao cadastrar Fabrico com CNPJ inválido** - Valida rejeição de CNPJ no formato incorreto
3. ✅ **Falha ao cadastrar Fabrico com ID duplicado** - Valida que não permite IDs duplicados
4. ✅ **Editar apenas o nome com sucesso** - Valida edição parcial mantendo campos não alterados
5. ✅ **Falha ao editar Fabrico inexistente** - Valida mensagem de erro ao editar Fabrico que não existe

## Como Executar os Testes

### Executar todos os testes do Fabrico

```bash
mvn test -Dtest=FabricoTestRunner
```

### Executar todos os testes do módulo

```bash
mvn test
```

### Executar com mais detalhes (verbose)

```bash
mvn test -Dtest=FabricoTestRunner -X
```

## Dependências

Os testes utilizam:

- **JUnit 5** (Jupiter) - Framework de testes
- **Cucumber 7.15.0** - Framework BDD
  - cucumber-java
  - cucumber-junit-platform-engine
- **Apache Commons Validator** - Validações adicionais

## Estrutura do Código

### Step Definitions

As step definitions seguem o padrão:

```java
@Dado("que o sistema está ativo no ano de {int}")
public void que_o_sistema_esta_ativo_no_ano_de(Integer ano) {
    // Setup inicial
}

@Quando("eu cadastro um novo Fabrico com os seguintes dados:")
public void eu_cadastro_um_novo_fabrico_com_os_seguintes_dados(DataTable dataTable) {
    // Ação de cadastro
}

@Então("o Fabrico {string} deve ser salvo com sucesso")
public void o_fabrico_deve_ser_salvo_com_sucesso(String fabricoId) {
    // Validação do resultado
}
```

### Inicialização

O hook `@Before` garante que cada cenário tenha uma instância limpa do repositório:

```java
@Before
public void setUp() {
    this.fabricoRepository = new FabricoRepositorioMemoria();
    this.fabricoService = new FabricoService(fabricoRepository);
    this.excecaoCapturada = null;
}
```

## Regras de Negócio Testadas

### Cadastro
- ID único obrigatório (ValueObject FabricoId)
- Nome fantasia obrigatório
- CNPJ obrigatório e deve seguir o padrão "XX.XXX.XXX/XXXX-XX"
- Não permitir cadastro com ID duplicado
- Validação de CNPJ no construtor da entidade

### Edição
- Preservar campos não informados na edição
- Validar existência do Fabrico antes de editar
- Permitir edição parcial dos dados (nome fantasia e/ou CNPJ)
- ID é imutável (final no construtor)

### Invariantes do Aggregate
- ID não pode ser nulo
- Nome fantasia não pode ser nulo ou vazio
- CNPJ não pode ser nulo ou vazio
- CNPJ deve estar no formato correto
- Igualdade baseada apenas no ID (equals/hashCode)

## Resultados dos Testes

Após a execução, você verá um resumo como:

```
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

Os relatórios detalhados ficam em:
```
target/surefire-reports/
```

## Troubleshooting

### Erro: "Cannot find package dev.com.confectextil"
- Verifique se o módulo foi compilado: `mvn clean compile`

### Erro: "No tests found"
- Verifique se o arquivo `cucumber.properties` está configurado corretamente
- Confirme que o arquivo `.feature` está no caminho correto

### Erro de CNPJ inválido não detectado
- Revise a regex no `FabricoService`: `\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}`

## Autor

Projeto de Requisitos - Grupo 6
