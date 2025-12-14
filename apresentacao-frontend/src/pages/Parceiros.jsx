import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { parceiroService } from "../services/parceiroService";
import "./parceiros.css";

export default function Parceiros() {
  const navigate = useNavigate();

  const [parceiros, setParceiros] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingParceiro, setEditingParceiro] = useState(null);
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

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = async (e) => {
    e.preventDefault();
    const isEditando = Boolean(editingParceiro);
    const payload = {
      id: (formData.id || "").trim(),
      nome: (formData.nome || "").trim(),
      telefone: (formData.telefone || "").trim(),
    };

    if ((!isEditando && !payload.id) || !payload.nome || !payload.telefone) {
      alert("Todos os campos são obrigatórios!");
      return;
    }
    try {
      let parceiroSalvo;
      if (isEditando) {
        parceiroSalvo = await parceiroService.editar(editingParceiro.id, {
          ...payload,
          id: editingParceiro.id,
        });
        setParceiros((prev) =>
          prev.map((p) => (p.id === editingParceiro.id ? parceiroSalvo : p))
        );
      } else {
        parceiroSalvo = await parceiroService.cadastrar(payload);
        setParceiros((prev) => [...prev, parceiroSalvo]);
      }
      setShowModal(false);
      setFormData({ id: "", nome: "", telefone: "" });
      setEditingParceiro(null);
      mostrarNotificacao(parceiroSalvo, isEditando);
    } catch (error) {
      const msg =
        error?.response?.data?.mensagem ||
        error?.response?.data?.message ||
        error?.message ||
        "Erro ao salvar parceiro.";
      alert(msg);
    }
  };

  const mostrarNotificacao = (parceiro, isEdicao) => {
    setNotificacao({
      titulo: isEdicao ? "Parceiro atualizado!" : "Salvo com Sucesso!",
      detalhes: `ID: ${parceiro.id} | Nome: ${parceiro.nome}`
    });
    setTimeout(() => setNotificacao(null), 4000);
  };

  const abrirModalNovo = () => {
    setEditingParceiro(null);
    setFormData({ id: "", nome: "", telefone: "" });
    setShowModal(true);
  };

  const fecharModal = () => {
    setShowModal(false);
    setEditingParceiro(null);
    setFormData({ id: "", nome: "", telefone: "" });
  };

  const abrirModalEdicao = (parceiro) => {
    setEditingParceiro(parceiro);
    setFormData({
      id: parceiro.id || "",
      nome: parceiro.nome || "",
      telefone: parceiro.telefone || "",
    });
    setShowModal(true);
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
          <button className="btn-pill" onClick={abrirModalNovo}>
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
              <div className="card-actions">
                <button
                  className="btn-edit"
                  type="button"
                  onClick={() => abrirModalEdicao(parceiro)}
                >
                  Editar
                </button>
              </div>
            </div>
          ))
        )}
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal">
            <h2>{editingParceiro ? "Editar Parceiro" : "Novo Parceiro"}</h2>
            <form onSubmit={handleSave}>
              <div className="form-group">
                <label>ID *</label>
                <input
                  className="form-input"
                  name="id"
                  value={formData.id}
                  onChange={handleInputChange}
                  placeholder="Digite o ID"
                  required={!editingParceiro}
                  disabled={Boolean(editingParceiro)}
                />
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
                <button type="button" className="btn-cancel" onClick={fecharModal}>Cancelar</button>
                <button type="submit" className="btn-save">Salvar</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </section>
  );
}
