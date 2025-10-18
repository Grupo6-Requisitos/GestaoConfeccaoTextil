# language: pt
Funcionalidade: Editar modelo
  Como usuário do sistema
  Quero editar os dados de um modelo existente
  Para manter as informações corretas e evitar duplicidades

  Contexto:
    Dado que existem os seguintes modelos cadastrados:
      | modeloId | referencia | nome              |
      | M-001    | MOD-001    | Camiseta Básica   |
      | M-002    | MOD-002    | Calça Jeans       |
      | M-003    | MOD-003    | Jaqueta Couro     |


  @sucesso
  Cenário: Editar apenas o nome do modelo
    Dado que desejo editar o modelo com referência "MOD-001"
    E informo o novo nome "Camiseta Premium"
    Quando o usuário solicita a edição do modelo
    Então o modelo deve ser atualizado com sucesso
    E os dados do modelo devem ser:
      | referencia | nome             |
      | MOD-001    | Camiseta Premium |

  @sucesso
  Cenário: Editar apenas a referência do modelo
    Dado que desejo editar o modelo com referência "MOD-002"
    E informo a nova referência "MOD-200"
    Quando o usuário solicita a edição do modelo
    Então o modelo deve ser atualizado com sucesso
    E os dados do modelo devem ser:
      | referencia | nome        |
      | MOD-200    | Calça Jeans |

  @sucesso
  Cenário: Editar nome e referência do modelo
    Dado que desejo editar o modelo com referência "MOD-003"
    E informo o novo nome "Jaqueta de Couro Premium"
    E informo a nova referência "MOD-300"
    Quando o usuário solicita a edição do modelo
    Então o modelo deve ser atualizado com sucesso
    E os dados do modelo devem ser:
      | referencia | nome                      |
      | MOD-300    | Jaqueta de Couro Premium  |

  @erro
  Cenário: Tentar editar o nome de um modelo inexistente
    Dado que desejo editar o modelo com referência "MOD-999"
    E informo o novo nome "Camiseta Fantasia"
    Quando o usuário solicita a edição do modelo
    Então deve ocorrer um erro com a mensagem "O modelo com referência MOD-999 não foi encontrado"

  @erro
  Cenário: Tentar alterar a referência para uma que já existe
    Dado que desejo editar o modelo com referência "MOD-001"
    E informo a nova referência "MOD-002"
    Quando o usuário solicita a edição do modelo
    Então deve ocorrer um erro com a mensagem "Já existe um modelo com a referência MOD-002"