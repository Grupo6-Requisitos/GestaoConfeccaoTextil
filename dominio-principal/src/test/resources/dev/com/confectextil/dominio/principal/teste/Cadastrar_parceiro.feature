Feature: Cadastro de Parceiro
  Como usuário do sistema
  Quero cadastrar um parceiro
  Para que ele seja registrado e possa ser consultado depois

  Scenario: Cadastrar um parceiro valido
    Given que eu tenho um ID "P001", nome "Thiago" e telefone "81999998888"
    When eu cadastrar o parceiro
    Then o parceiro com ID "P001" deve estar registrado no sistema
    And o nome do parceiro deve ser "Thiago"
    And o telefone do parceiro deve ser "81999998888"

  Scenario: Tentar cadastrar parceiro com nome invalido
    Given que eu tenho um ID "P002", nome "" e telefone "81999998888"
    When eu cadastrar o parceiro
    Then uma excecao deve ser lancada com a mensagem "O nome de parceiro esta vazio"

  Scenario: Tentar cadastrar parceiro com telefone invalido
    Given que eu tenho um ID "P003", nome "Maria" e telefone "12345"
    When eu cadastrar o parceiro
    Then uma excecao deve ser lancada com a mensagem "O telefone deve conter exatamente 11 digitos numericos"