Padrão de Projeto Observer

**Implementação:**
- O padrão Observer foi utilizado no fluxo de cadastro de Parceiros. O objetivo foi desacoplar a regra de negócio principal (salvar o parceiro no banco) de ações secundárias ou efeitos colaterais (como logs, notificações ou envios de e-mail). Assim, o ParceiroService não precisa conhecer os detalhes de quem precisa ser notificado.

**Classes Criadas:**
- ObservadorNovoParceiro (Interface): Define o contrato que todos os observadores devem seguir método executadoAposCadastro.
- LogNovoParceiroObserver (Observer Concreto): Implementação que realiza a ação de logar no console quando um novo parceiro é criado.

**Classes Alteradas:**
- ParceiroService:
  - Foi alterada para receber uma lista de **ObservadorNovoParceiro** no construtor.
  - Foi adicionado o método privado **notificarObservadores()**.
  - O método **cadastrar()** foi modificado para chamar a notificação após o salvamento bem-sucedido. 

- AplicacaoBackend:
  - Alterada para injetar a lista de observadores ao instanciar o **ParceiroService**.


Strategy


**Contexto da Adoção:**
O padrão Strategy foi utilizado no domínio de **Etapas**. O objetivo foi permitir que diferentes tipos de etapas (ex: Padrão, Produção, Qualidade) tenham regras de validação ou processamento distintos, sem encher o código principal de **if/else** complexos. O serviço seleciona a estratégia adequada com base no tipo da etapa.

**Classes Criadas:**
- tapaStrategy (Interface): Define o contrato para as regras de negócio de uma etapa (métodos seAplica e processarRegras).
- (Classes concretas de estratégia implementando a interface acima, ex: EstrategiaEtapaPadrao, se houver).

**Classes Alteradas:**
- EtapaService (Context):
  - Alterada para conter uma lista de EtapaStrategy.
  - No método cadastrarEtapa, foi adicionada a lógica que percorre a lista de estratégias, encontra a correta para o tipo informado e executa a regra de negócio.