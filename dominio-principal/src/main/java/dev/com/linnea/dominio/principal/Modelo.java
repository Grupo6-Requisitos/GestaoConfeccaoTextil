package dev.com.linnea.dominio.principal;

public class Modelo {
    private int id;
    private String referencia;
    private String nome;
    private String imagemUrl;

    public Modelo(int id, String nome, String referencia, String imagemUrl) {
        this.id = id;
        this.nome = nome;
        this.referencia = referencia;
        this.imagemUrl = imagemUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }
}

public class Jogador {

    private int id;
    private Contrato contrato;
    private Clube clube;
    private Competicao competicao;
    private String nome;
    private int idade;
    private String posicao;
    private String perna;
    private double nota;
    private int jogos;
    private int gols;
    private int assistencias;
    private String status;
    private String minutagem;   
    private int anosDeClube;     
    private boolean capitao;    
    private int grauLesao = -1;         
    private boolean contratoAtivo = false;
    private boolean saudavel;

    public Jogador(int id, Contrato contrato, Clube clube, Competicao competicao, String nome, int idade,
                   String posicao, String perna, double nota, int jogos, int gols, int assistencias,
                   String minutagem, int anosDeClube, boolean capitao) {
        this.id = id;
        this.contrato = contrato;
        this.clube = clube;
        this.competicao = competicao;
        this.nome = nome;
        this.idade = idade;
        this.posicao = posicao;
        this.perna = perna;
        this.nota = nota;
        this.jogos = jogos;
        this.gols = gols;
        this.assistencias = assistencias;
        this.minutagem = minutagem;
        this.anosDeClube = anosDeClube;
        this.capitao = capitao;
    }

    public Jogador(int id, Contrato contrato, Clube clube, Competicao competicao, String nome, int idade,
                   String posicao, String perna, double nota, int jogos, int gols, int assistencias) {
        this(id, contrato, clube, competicao, nome, idade, posicao, perna, nota, jogos, gols, assistencias,
             null, 0, false);
    }

    public Jogador(String nome, Clube clube) {
        this.nome = nome;
        this.clube = clube;
        if (clube != null) {
            this.contrato = new Contrato(clube);
        }
        this.minutagem = null;
        this.anosDeClube = 0;
        this.capitao = false;
    }

    public Jogador(Contrato contrato) {
        this.contrato = contrato;
        this.minutagem = null;
        this.anosDeClube = 0;
        this.capitao = false;
    }

    public Jogador(String nome) {
        this.nome = nome;
        this.minutagem = null;
        this.anosDeClube = 0;
        this.capitao = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Contrato getContrato() { return contrato; }
    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
        if (contrato != null) {
            this.clube = contrato.getClube();
        }
    }

    public Clube getClube() { return clube; }
    public void setClube(Clube clube) { this.clube = clube; }

    public Competicao getCompeticao() { return competicao; }
    public void setCompeticao(Competicao competicao) { this.competicao = competicao; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }

    public String getPosicao() { return posicao; }
    public void setPosicao(String posicao) { this.posicao = posicao; }

    public String getPerna() { return perna; }
    public void setPerna(String perna) { this.perna = perna; }

    public double getNota() { return nota; }
    public void setNota(double nota) { this.nota = nota; }

    public int getJogos() { return jogos; }
    public void setJogos(int jogos) { this.jogos = jogos; }

    public int getGols() { return gols; }
    public void setGols(int gols) { this.gols = gols; }

    public int getAssistencias() { return assistencias; }
    public void setAssistencias(int assistencias) { this.assistencias = assistencias; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isDisponivel() {
        return "disponível".equalsIgnoreCase(this.status);
    }

    public String getMinutagem() { return minutagem; }
    public void setMinutagem(String minutagem) { this.minutagem = minutagem; }

    public int getAnosDeClube() { return anosDeClube; }
    public void setAnosDeClube(int anosDeClube) { this.anosDeClube = anosDeClube; }

    public boolean isCapitao() { return capitao; }
    public void setCapitao(boolean capitao) { this.capitao = capitao; }

    public int getGrauLesao() {
        return grauLesao;
    }

    public void setGrauLesao(int grauLesao) {
        this.grauLesao = grauLesao;
    }

    public boolean isContratoAtivo() {
        return contratoAtivo;
    }

    public void setContratoAtivo(boolean contratoAtivo) {
        this.contratoAtivo = contratoAtivo;
    }

    public boolean isSaudavel() {
        return saudavel;
    }

    public void setSaudavel(boolean saudavel) {
        this.saudavel = saudavel;
    }
}