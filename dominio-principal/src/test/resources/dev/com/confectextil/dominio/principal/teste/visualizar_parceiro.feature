Feature: Visualizar Parceiros
  Como usuário do sistema
  Quero visualizar os parceiros
  Para que eu possa saber quais são os parceiros

  Scenario: Existem parceiros cadastrados
    Given que existem parceiros cadastrados no sistema
    When solicito para listar todos os parceiros
    Then o sistema deve exibir a lista de parceiros com seus nomes e telefones

  Scenario: Nao existir parceiros cadastrados
    Given que nao existem parceiros cadastrados no sistema
    When solicito para listar todos os parceiros
    Then o sistema deve informar que nao existem parceiros cadastrados