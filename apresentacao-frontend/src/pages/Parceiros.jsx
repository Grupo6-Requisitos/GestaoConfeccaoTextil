import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { parceiroService } from "../services/parceiroService";
import "./parceiros.css";

export default function Parceiros() {
  const navigate = useNavigate();

  const [parceiros, setParceiros] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({ id: "", nome: "", telefone: "" });
  const [notificacao, setNotificacao] = useState(null);

  useEffect(() => {
    carregarParceiros();
  }, []);

  const carregarParceiros = async () => {
    setLoading(true);
    try {
      const dados = await parceiroService.listar();
      setParceiros(dados);
    } catch (error) {
      console.error("Erro ao carregar", error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (confirm("Deseja realmente excluir este parceiro?")) {
      try {
        await parceiroService.excluir(id);
        setParceiros((prev) => prev.filter((p) => p.id !== id));
      } catch (error) {
        alert("Erro ao excluir parceiro.");
      }
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = async (e) => {
    e.preventDefault();
    if (!formData.id || !formData.nome || !formData.telefone) {
      alert("Todos os campos são obrigatórios!");
      return;
    }
    try {
      const novoParceiro = await parceiroService.cadastrar(formData);
      setParceiros((prev) => [...prev, novoParceiro]);
      setShowModal(false);
      setFormData({ id: "", nome: "", telefone: "" });
      mostrarNotificacao(novoParceiro);
    } catch (error) {
      alert("Erro ao salvar. Verifique se o ID já existe.");
    }
  };

  const mostrarNotificacao = (parceiro) => {
    setNotificacao({
      titulo: "Salvo com Sucesso!",
      detalhes: `ID: ${parceiro.id} | Nome: ${parceiro.nome}`
    });
    setTimeout(() => setNotificacao(null), 4000);
  };

  return (
    <section className="content-container">

      {notificacao && (
        <div className="toast-success">
          <span className="toast-title">✅ {notificacao.titulo}</span>
          <span className="toast-details">{notificacao.detalhes}</span>
        </div>
      )}

      <div className="content-header" style={{ marginBottom: 0 }}>
        <div className="title-wrap">

          {/* --- 3. EVENTO ONCLICK NA SETA --- */}
          <svg
            onClick={() => navigate('/')} /* Redireciona para a raiz (Home) */
            width="24" height="24" viewBox="0 0 24 24" fill="none"
            style={{marginRight: 8, cursor: 'pointer'}}
          >
             <path d="M19 12H5" stroke="#111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
             <path d="M12 19l-7-7 7-7" stroke="#111" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>

          <h1 style={{ fontFamily: '"Courier New", Courier, monospace', fontWeight: 'bold' }}>Parceiros</h1>
        </div>

        <div className="controls">
          <button className="btn-pill" onClick={() => setShowModal(true)}>
            Adicionar parceiros +
          </button>
        </div>
      </div>

      <div className="grid-parceiros">
        {loading ? (
          <p>Carregando...</p>
        ) : parceiros.length === 0 ? (
          <p>Nenhum parceiro cadastrado.</p>
        ) : (
          parceiros.map((parceiro) => (
            <div key={parceiro.id} className="parceiro-card">
              <div className="parceiro-info">
                <div>
                  <div className="info-label">id</div>
                  <div className="info-value">{parceiro.id}</div>
                </div>
                <div>
                  <div className="info-label">Nome</div>
                  <div className="info-value">{parceiro.nome}</div>
                </div>
                <div>
                  <div className="info-label">Telefone</div>
                  <div className="info-value">{parceiro.telefone}</div>
                </div>
              </div>
              <button className="btn-trash" onClick={() => handleDelete(parceiro.id)}>
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <polyline points="3 6 5 6 21 6"></polyline>
                  <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                </svg>
              </button>
            </div>
          ))
        )}
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal">
            <h2>Novo Parceiro</h2>
            <form onSubmit={handleSave}>
              <div className="form-group">
                <label>ID *</label>
                <input className="form-input" name="id" value={formData.id} onChange={handleInputChange} placeholder="Digite o ID" required />
              </div>
              <div className="form-group">
                <label>Nome *</label>
                <input className="form-input" name="nome" value={formData.nome} onChange={handleInputChange} placeholder="Ex: Confecção Silva" required />
              </div>
              <div className="form-group">
                <label>Telefone *</label>
                <input className="form-input" name="telefone" value={formData.telefone} onChange={handleInputChange} placeholder="Ex: 81999998888" required />
              </div>
              <div className="modal-actions">
                <button type="button" className="btn-cancel" onClick={() => setShowModal(false)}>Cancelar</button>
                <button type="submit" className="btn-save">Salvar</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </section>
  );
}