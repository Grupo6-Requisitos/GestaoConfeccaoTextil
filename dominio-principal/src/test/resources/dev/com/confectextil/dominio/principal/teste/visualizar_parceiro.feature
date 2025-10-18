# language: pt

Funcionalidade: Visualizar Parceiros
  Como usuário do sistema
  Quero visualizar os parceiros
  Para que eu possa saber quais são os parceiros

  Regra:
    - O sistema deve listar todos os parceiros cadastrados, mostrando nome e telefone.
    - Caso não existam parceiros cadastrados, deve ser exibida uma mensagem informando isso.

  Cenario: Existem parceiros cadastrados
    Dado que existem parceiros cadastrados no sistema
    Quando solicito para listar todos os parceiros
    Entao o sistema deve exibir a lista de parceiros com seus nomes e telefones

  Cenario: Nao existir parceiros cadastrados
    Dado que nao existem parceiros cadastrados no sistema
    Quando solicito para listar todos os parceiros
    Entao o sistema deve informar que nao existem parceiros cadastrados