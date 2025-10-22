# language: pt

Funcionalidade: Cadastro de Parceiro
  Como usuário do sistema
  Quero cadastrar um parceiro
  Para que ele seja registrado e possa ser consultado depois

  Regra:
    - Todo parceiro deve ter um ID(identificador) válido e único.
    - O nome do parceiro não pode ser vazio e deve conter no mínimo 3 caracteres.
    - O telefone do parceiro deve conter exatamente 11 dígitos numéricos.
    - Não é permitido cadastrar dois parceiros com o mesmo ID(identificador).

  Cenario: Cadastrar um parceiro valido
    Dado que eu tenho um identificador "P001", nome "Thiago" e telefone "81999998888"
    Quando eu cadastrar o parceiro
    Entao o parceiro com identificador "P001" deve estar registrado no sistema
    E o nome do parceiro deve ser "Thiago"
    E o telefone do parceiro deve ser "81999998888"

  Cenario: Tentar cadastrar parceiro com nome invalido
    Dado que eu tenho um identificador "P002", nome "" e telefone "81999998888"
    Quando eu cadastrar o parceiro
    Entao uma excecao deve ser lancada com a mensagem "O nome de parceiro esta vazio"

  Cenario: Tentar cadastrar parceiro com telefone invalido
    Dado que eu tenho um identificador "P003", nome "Maria" e telefone "12345"
    Quando eu cadastrar o parceiro
    Entao uma excecao deve ser lancada com a mensagem "O telefone deve conter exatamente 11 digitos numericos"