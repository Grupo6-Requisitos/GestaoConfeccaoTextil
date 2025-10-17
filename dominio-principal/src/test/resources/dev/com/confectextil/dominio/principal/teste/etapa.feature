# language: pt

Funcionalidade: Gestão de Etapas do Processo Produtivo
  Como administrador do sistema
  Desejo cadastrar, editar e reordenar as etapas do processo produtivo
  Para registrar e gerenciar corretamente o fluxo de produção de confecção.

  Regras Gerais:
    - O ID da etapa deve ser único.
    - O Nome da etapa é obrigatório.
    - A Ordem deve ser um número inteiro positivo e único entre as etapas.

  Contexto:
    Dado que o sistema de gestão de produção está ativo

  # -------------------------------------------------------------------------
  # Cenários de Cadastro (Funcionalidade 1)
  # -------------------------------------------------------------------------

  Cenário: Cadastrar nova etapa com sucesso
    Dado que não existe uma etapa cadastrada com id "ETA-001"
    Quando eu cadastro uma nova etapa com os seguintes dados:
      | id      | nome            | ordem |
      | ETA-001 | Corte de Tecido | 1     |
    Então a etapa "ETA-001" deve ser salva com sucesso
    E o nome da etapa deve ser "Corte de Tecido"
    E a ordem deve ser 1

  Cenário: Falha ao cadastrar etapa com ID duplicado
    Dado que já existe uma etapa cadastrada com id "ETA-002"
    Quando eu tento cadastrar outra etapa com id "ETA-002"
    Então o sistema deve rejeitar o cadastro da Etapa
    E a exceção deve conter a mensagem "Etapa já cadastrada"  

  Cenário: Falha ao cadastrar etapa sem nome
    Quando eu tento cadastrar uma etapa com os seguintes dados:
      | id      | nome | ordem |
      | ETA-003 |      | 2     | 
    Então o sistema deve rejeitar o cadastro da Etapa
    # CORRIGIDO: Padronizado para "O nome da Etapa é obrigatório."
    E a exceção deve conter a mensagem "O nome da Etapa é obrigatório."

  Cenário: Falha ao cadastrar etapa com ordem inválida (não positiva)
    Quando eu tento cadastrar uma etapa com os seguintes dados:
      | id      | nome          | ordem |
      | ETA-004 | Preparação    | 0     |
    Então o sistema deve rejeitar o cadastro da Etapa
    # CORRIGIDO: Padronizado para "A ordem da Etapa deve ser um número positivo."
    E a exceção deve conter a mensagem "A ordem da Etapa deve ser um número positivo."

  # -------------------------------------------------------------------------
  # Cenários de Edição (Funcionalidade 2)
  # -------------------------------------------------------------------------

  Cenário: Editar apenas o nome da etapa com sucesso
    Dado que existe uma etapa com os seguintes dados:
      | id      | nome            | ordem |
      | ETA-005 | Costura Básica  | 3     |
    Quando eu edito a etapa "ETA-005" alterando:
      | nome | Costura Final |
    Então a etapa "ETA-005" deve ter o nome "Costura Final"
    E a ordem deve permanecer 3

  Cenário: Falha ao editar etapa inexistente
    Dado que não existe uma etapa cadastrada com id "ETA-999"
    Quando eu tento editar a etapa "ETA-999" alterando:
      | nome | Revisão Final |
    Então o sistema deve rejeitar a operação 
    E a exceção deve conter a mensagem "Etapa não encontrada"

  # -------------------------------------------------------------------------
  # Cenários de Reordenação (Funcionalidade 3)
  # -------------------------------------------------------------------------

  Cenário: Reordenar etapas com sucesso
    Dado que existem as etapas com os seguintes dados e ordem:
      | id      | nome    | ordem |
      | ETA-010 | Corte   | 1     |
      | ETA-011 | Costura | 2     |
      | ETA-012 | Revisão | 3     |
    Quando eu atualizo a ordem das etapas com os seguintes dados:
      | id      | novaOrdem |
      | ETA-012 | 1         |
      | ETA-010 | 2         |
      | ETA-011 | 3         |
    Então as etapas devem ser reordenadas com sucesso
    E a etapa "ETA-012" deve ter a ordem 1
    E a etapa "ETA-010" deve ter a ordem 2
    E a etapa "ETA-011" deve ter a ordem 3

  Cenário: Falha ao reordenar com valores de ordem duplicados
    Dado que existem as etapas com os seguintes dados:
      | id      | nome       | ordem |
      | ETA-020 | Embalagem  | 4     |
      | ETA-021 | Transporte | 5     |
    Quando eu tento atualizar a ordem das etapas com os seguintes dados:
      | id      | novaOrdem |
      | ETA-020 | 1         |
      | ETA-021 | 1         |
    Então o sistema deve rejeitar a operação
    E a exceção deve conter a mensagem "Valores de ordem duplicados não são permitidos"