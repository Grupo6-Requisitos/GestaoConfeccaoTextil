# language: pt
Funcionalidade: Cadastro de Modelos
  Como um Gestor do Fabrico,
  Eu quero poder cadastrar novos modelos de produtos,
  Para que eu possa reutilizá-los em futuras fichas técnicas de produção.

    Cenario: Cadastrar um novo modelo com sucesso, incluindo sua lista de insumos padrão
        Dado que eu sou um usuário "GESTOR" autenticado
        E existem os seguintes insumos cadastrados no sistema:
        | referencia | nome             | unidade |
        | TEC-01     | Malha de Algodão | m       |
        | LIN-02     | Linha Branca     | m       |
        Quando eu tento cadastrar um novo modelo com os seguintes dados:
        | campo      | valor                           |
        | referencia | MOD-001                         |
        | nome       | Camiseta Gola V Padrão          |
        | imagemUrl  | http://exemplo.com/camiseta.jpg |
        And com a seguinte lista de insumos padrão:
        | insumo_referencia | quantidade_sugerida |
        | TEC-01            | 1.2                   |
        | LIN-02            | 150.0                 |
        Entao o modelo com a referência "MOD-001" deve ser salvo com sucesso
        E o modelo salvo deve ter o nome "Camiseta Gola V Padrão"
        E o modelo salvo deve ter 2 insumos padrão
        E o modelo salvo deve conter o insumo de referência "TEC-01" com quantidade sugerida 1.2

    Cenario: Tentar cadastrar um modelo com uma referência que já existe
        Dado que eu sou um usuário "GESTOR" autenticado
        E já existe um modelo cadastrado com a referência "MOD-002"
        Quando eu tento cadastrar um novo modelo com os seguintes dados:
        | campo      | valor                           |
        | referencia | MOD-002                         |
        | nome       | Camiseta Gola Redonda           |
        | imagemUrl  | http://exemplo.com/imagem2.jpg  |
        Entao o sistema deve retornar um erro informando que "A referência do modelo já existe"

    Cenario: O novo modelo cadastrado deve aparecer na listagem geral
        Dado que eu sou um usuário "GESTOR" autenticado
        E existem os seguintes modelos já cadastrados:
        | referencia | nome                |
        | MOD-003    | Blusa de Alcinha    |
        | MOD-004    | Calça Jeans Skinny  |
        Quando eu cadastro com sucesso o novo modelo com referência "MOD-005" e nome "Shorts de Corrida"
        And eu peço a lista completa de modelos
        Entao a lista de modelos deve conter 3 itens
        And a lista de modelos deve incluir um modelo com referência "MOD-005" e nome "Shorts de Corrida"

    Cenario: Tentar cadastrar um modelo com referência em branco
        Dado que eu sou um usuário "GESTOR" autenticado
        Quando eu tento cadastrar um novo modelo com os seguintes dados:
        | campo      | valor           |
        | referencia |                 |
        | nome       | Modelo Sem Ref  |
        Entao o sistema deve retornar um erro informando que "A referência do modelo é obrigatória"

    Cenario: Tentar cadastrar um modelo com nome em branco
        Dado que eu sou um usuário "GESTOR" autenticado
        Quando eu tento cadastrar um novo modelo com os seguintes dados:
        | campo      | valor           |
        | referencia | MOD-999         |
        | nome       |                 |
        Entao o sistema deve retornar um erro informando que "O nome do modelo é obrigatório"

    Cenario: Tentar cadastrar um modelo usando um insumo que não existe
        Dado que eu sou um usuário "GESTOR" autenticado
        E não existe um insumo com a referência "INVALIDO-01"
        Quando eu tento cadastrar um novo modelo com os seguintes dados:
        | campo      | valor           |
        | referencia | MOD-998         |
        | nome       | Modelo Invalido |
        And com a seguinte lista de insumos padrão:
        | insumo_referencia | quantidade_sugerida |
        | INVALIDO-01       | 1.0                   |
        Entao o sistema deve retornar um erro informando que "O insumo com referência INVALIDO-01 não foi encontrado"