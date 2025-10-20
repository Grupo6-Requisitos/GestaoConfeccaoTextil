# language: pt
Funcionalidade: Edição de Parceiros
  Como um Gestor do Fabrico,
  Eu quero poder editar as informações de um parceiro já cadastrado,
  Para manter os dados de contato atualizados.

  Cenario: Editar nome e telefone de um parceiro com sucesso
    Dado que existe um Parceiro cadastrado com id "P-001", nome "Facção da Maria" e telefone "11987654321"
    Quando eu edito o Parceiro "P-001" alterando:
      | nome     | Facção da Maria LTDA |
      | telefone | 11999998888          |
    Entao o parceiro com id "P-001" deve ser atualizado com sucesso
    E o nome do parceiro deve ser "Facção da Maria LTDA"
    E o telefone do parceiro deve ser "11999998888"

  Cenario: Tentar editar um parceiro que não existe
    Dado que não existe um Parceiro com id "P-999"
    Quando eu tento editar o Parceiro "P-999" alterando:
      | nome | Parceiro Fantasma |
    Entao uma excecao deve ser lancada com a mensagem "Parceiro não encontrado"

  Cenario: Tentar editar um parceiro com um telefone inválido
    Dado que existe um Parceiro cadastrado com id "P-002", nome "Lavanderia Azul" e telefone "22987654321"
    Quando eu tento editar o Parceiro "P-002" alterando:
      | telefone | 12345 |
    Entao uma excecao deve ser lancada com a mensagem "O telefone deve conter exatamente 11 digitos numericos"