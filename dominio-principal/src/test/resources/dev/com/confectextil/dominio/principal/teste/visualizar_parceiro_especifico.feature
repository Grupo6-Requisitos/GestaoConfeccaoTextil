# language: pt
Funcionalidade: Visualização de Parceiro Específico
  Como um usuário do sistema,
  Eu quero poder buscar um parceiro por seu ID,
  Para consultar seus detalhes específicos.

  Cenario: Buscar um parceiro que existe
    Dado que existe um Parceiro cadastrado com id "P-123", nome "Transportadora Veloz" e telefone "11912345678"
    Quando eu solicitar os detalhes do parceiro com id "P-123"
    Entao eu devo receber os detalhes do parceiro
    E o nome do parceiro deve ser "Transportadora Veloz"
    E o telefone do parceiro deve ser "11912345678"

  Cenario: Tentar buscar um parceiro que não existe
    Dado que não existe um Parceiro com id "P-999"
    Quando eu tento solicitar os detalhes do parceiro com id "P-999"
    Entao uma excecao deve ser lancada com a mensagem "Parceiro não encontrado"