Feature: Editar modelo

  Scenario: Editar apenas o nome com sucesso
    Given que existe um modelo com modelosId "M-001", referenciaInterna "REF-10", imagemUrl "https://img/a.png" e nome "Camiseta Lisa"
    When eu altero o nome para "Camiseta Lisa Premium"
    Then o sistema salva o modelo com nome "Camiseta Lisa Premium"
    And a referenciaInterna permanece "REF-10"
    And a imagemUrl permanece "https://img/a.png"
