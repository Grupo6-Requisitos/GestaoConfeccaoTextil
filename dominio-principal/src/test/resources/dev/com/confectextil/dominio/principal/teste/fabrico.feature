# language: pt

Funcionalidade: 1 - Cadastro e Edição de Fabrico
  Como administrador do sistema
  Desejo cadastrar e editar informações de empresas têxteis (Fabrico)
  Para garantir o correto gerenciamento das fábricas no sistema

  Regra: O cadastro do Fabrico deve ter um ID único e um CNPJ válido
        Não é permitido cadastrar uma empresa com o mesmo identificador
        E o CNPJ deve seguir o padrão "XX.XXX.XXX/XXXX-XX"

  Contexto:
    Dado que o sistema está ativo no ano de 2025

  Cenário: Cadastrar Fabrico com sucesso
    Dado que não existe um Fabrico com id "F-001"
    Quando eu cadastro um novo Fabrico com os seguintes dados:
      | fabricoId     | F-001              |
      | nomeFantasia  | Têxtil Brasil      |
      | cnpj          | 12.345.678/0001-90 |
    Então o Fabrico "F-001" deve ser salvo com sucesso
    E o CNPJ deve ser "12.345.678/0001-90"

  Cenário: Falha ao cadastrar Fabrico com CNPJ inválido
    Dado que não existe um Fabrico com id "F-002"
    Quando eu cadastro um novo Fabrico com os seguintes dados:
      | fabricoId     | F-002           |
      | nomeFantasia  | Fios do Norte   |
      | cnpj          | 123456789000190 |
    Então o sistema deve rejeitar o cadastro por CNPJ inválido

  Cenário: Falha ao cadastrar Fabrico com ID duplicado
    Dado que já existe um Fabrico cadastrado com id "F-003"
    Quando eu tento cadastrar outro Fabrico com id "F-003"
    Então o sistema deve rejeitar o cadastro
    E exibir a mensagem de erro "Fabrico já cadastrado"

  Regra: A edição do Fabrico deve preservar os campos não alterados
        Somente os campos informados na edição devem ser modificados

  Cenário: Editar apenas o nome com sucesso
    Dado que existe um Fabrico com os seguintes dados:
      | fabricoId     | F-004              |
      | nomeFantasia  | Têxtil do Sul      |
      | cnpj          | 22.333.444/0001-10 |
    Quando eu edito o Fabrico "F-004" alterando:
      | nomeFantasia | Têxtil do Sul Premium |
    Então o Fabrico deve ter o nome "Têxtil do Sul Premium"
    E o CNPJ deve permanecer "22.333.444/0001-10"

  Cenário: Falha ao editar Fabrico inexistente
    Dado que não existe um Fabrico com id "F-999"
    Quando eu tento editar o Fabrico "F-999" alterando:
      | nomeFantasia | Nova Fábrica XYZ |
    Então o sistema deve exibir a mensagem de erro "Fabrico não encontrado"
