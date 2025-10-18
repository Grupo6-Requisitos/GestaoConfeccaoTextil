# language: pt
Funcionalidade: Remover modelo
  Como usuário do sistema
  Quero remover um modelo existente
  Para manter o cadastro de modelos consistente e atualizado

  Contexto:
    Dado que existem os seguintes modelos cadastrados para remoção:
      | modeloId | referencia | nome              |
      | M-001    | MOD-001    | Camiseta Básica   |
      | M-002    | MOD-002    | Calça Jeans       |
      | M-003    | MOD-003    | Jaqueta Couro     |

  @sucesso
  Cenário: Remover um modelo existente por referência
    Dado que desejo remover o modelo com referência "MOD-002"
    Quando o usuário solicita a remoção do modelo
    Então o modelo deve ser removido com sucesso
    E o modelo com referência "MOD-002" não deve existir
    E os modelos restantes devem ser:
      | referencia | nome             |
      | MOD-001    | Camiseta Básica  |
      | MOD-003    | Jaqueta Couro    |

  @erro
  Cenário: Tentar remover um modelo inexistente
    Dado que desejo remover o modelo com referência "MOD-999"
    Quando o usuário solicita a remoção do modelo
    Então deve ocorrer um erro ao remover com a mensagem "O modelo com referência MOD-999 não foi encontrado"

  @erro
  Cenário: Tentar remover sem informar referência
    Dado que desejo remover o modelo com referência ""
    Quando o usuário solicita a remoção do modelo
    Então deve ocorrer um erro ao remover com a mensagem "Referência alvo não informada"
