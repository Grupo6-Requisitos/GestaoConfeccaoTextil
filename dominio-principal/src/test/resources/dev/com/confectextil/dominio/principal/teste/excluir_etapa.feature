# language: pt

Funcionalidade: Excluir Etapa
  Como usuário do sistema
  Quero excluir uma etapa
  Para manter o fluxo de produção atualizado

  Regras Gerais:
  - O ID da etapa deve ser único.
  - O Nome da etapa é obrigatório.
  - A Ordem deve ser um número inteiro positivo e único entre as etapas.

  Cenario: Excluir uma etapa existente
    Dado que existe uma etapa cadastrada com ID "E001"
    Quando eu solicitar a exclusao da etapa com ID "E001"
    Entao o sistema deve remover a etapa do cadastro

  Cenario: Tentar excluir uma etapa inexistente
    Dado que nao existe uma etapa cadastrada com ID "E999"
    Quando eu solicitar a exclusao da etapa com ID "E999"
    Entao o sistema deve informar "Etapa nao encontrada para exclusao."
