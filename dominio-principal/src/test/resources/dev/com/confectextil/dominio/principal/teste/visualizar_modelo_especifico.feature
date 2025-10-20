# language: pt
Funcionalidade: Visualizar modelo específico
  Como usuário do sistema
  Quero visualizar os dados de um modelo
  Para consultar suas informações por referência ou por modeloId

  Contexto:
    Dado que existem os seguintes modelos cadastrados para visualização:
      | modeloId | referencia | nome              |
      | M-001    | MOD-001    | Camiseta Básica   |
      | M-002    | MOD-002    | Calça Jeans       |
      | M-003    | MOD-003    | Jaqueta Couro     |

  @sucesso
  Cenário: Visualizar modelo por referência
    Dado que desejo visualizar o modelo pela referência "MOD-001"
    Quando o usuário solicita a visualização do modelo
    Então o modelo deve ser exibido com sucesso
    E os dados do modelo visualizado devem ser:
      | modeloId | referencia | nome             |
      | M-001    | MOD-001    | Camiseta Básica  |

  @sucesso
  Cenário: Visualizar modelo por modeloId
    Dado que desejo visualizar o modelo pelo modeloId "M-002"
    Quando o usuário solicita a visualização do modelo
    Então o modelo deve ser exibido com sucesso
    E os dados do modelo visualizado devem ser:
      | modeloId | referencia | nome        |
      | M-002    | MOD-002    | Calça Jeans |

  @erro
  Cenário: Tentar visualizar por referência inexistente
    Dado que desejo visualizar o modelo pela referência "MOD-999"
    Quando o usuário solicita a visualização do modelo
    Então deve ocorrer um erro ao visualizar com a mensagem "O modelo com referência MOD-999 não foi encontrado"

  @erro
  Cenário: Tentar visualizar por modeloId inexistente
    Dado que desejo visualizar o modelo pelo modeloId "M-999"
    Quando o usuário solicita a visualização do modelo
    Então deve ocorrer um erro ao visualizar com a mensagem "O modelo com id M-999 não foi encontrado"

  @erro
  Cenário: Tentar visualizar sem informar chave de busca
    Dado que desejo visualizar o modelo pela referência ""
    E que desejo visualizar o modelo pelo modeloId ""
    Quando o usuário solicita a visualização do modelo
    Então deve ocorrer um erro ao visualizar com a mensagem "É obrigatório informar referência ou modeloId"
