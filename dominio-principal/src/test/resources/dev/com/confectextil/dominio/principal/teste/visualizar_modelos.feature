# language: pt
Funcionalidade: Visualização de Modelos
  Como um Gestor do Fabrico,
  Eu quero poder visualizar a lista de todos os modelos de produtos cadastrados,
  Para que eu possa ter uma visão geral do que está disponível.

  Cenario: Listar modelos quando existem itens cadastrados
    Dado que existem os seguintes modelos já cadastrados no sistema:
      | referencia | nome                 |
      | MOD-10     | Camisa Social Slim   |
      | MOD-11     | Bermuda de Linho     |
      | MOD-12     | Vestido Longo Floral |
    Quando eu solicitar a lista de todos os modelos
    Entao a lista retornada deve conter 3 modelos, incluindo um com referência "MOD-11" e nome "Bermuda de Linho"

  Cenario: Listar modelos quando não há nenhum cadastrado
    Dado que não existem modelos cadastrados no sistema
    Quando eu solicitar a lista de todos os modelos
    Entao eu devo receber uma lista vazia