import React, { useState, useEffect } from "react";
import { etapaService } from "../services/etapaService";
import "./etapas.css";

export default function Etapas() {
  const fixedStart = { id: "init", title: "Iniciada", isFixed: true, tipo: "PADRAO" };
  const fixedEnd = { id: "final", title: "Finalizada", isFixed: true, tipo: "PADRAO" };

  const [columns, setColumns] = useState([fixedStart, fixedEnd]);
  const [showModal, setShowModal] = useState(false);

  // --- NOVO ESTADO: Controle de Edição ---
  const [isEditing, setIsEditing] = useState(false);

  const [formData, setFormData] = useState({
    id: "",
    nome: "",
    ordem: "",
    tipo: "PADRAO"
  });

  useEffect(() => {
    carregarEtapas();
  }, []);

  const carregarEtapas = async () => {
    try {
      const etapasDoBanco = await etapaService.listar();

      // Ordena para garantir que apareçam na ordem correta visualmente
      etapasDoBanco.sort((a, b) => a.ordem - b.ordem);

      const etapasDinamicas = etapasDoBanco.map(e => ({
        id: e.id,
        title: e.nome,
        tipo: e.tipo,
        isFixed: false,
        ordem: e.ordem
      }));
      setColumns([fixedStart, ...etapasDinamicas, fixedEnd]);
    } catch (error) {
      console.error("Erro ao buscar etapas:", error);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  // --- FUNÇÃO PARA ABRIR O MODAL EM MODO CRIAÇÃO ---
  const handleOpenCreate = () => {
    setIsEditing(false); // Garante que não é edição
    setFormData({ id: "", nome: "", ordem: "", tipo: "PADRAO" }); // Limpa
    setShowModal(true);
  };

  // --- FUNÇÃO PARA ABRIR O MODAL EM MODO EDIÇÃO ---
  const handleEditClick = (col) => {
    setIsEditing(true); // Marca que é edição
    setFormData({
      id: col.id,
      nome: col.title, // Note que no objeto visual chamamos de 'title', mas no form é 'nome'
      tipo: col.tipo || "PADRAO",
      ordem: col.ordem
    });
    setShowModal(true);
  };

  const handleSaveColumn = async (e) => {
    e.preventDefault();

    if (!formData.id || !formData.nome || !formData.ordem) {
      alert("Preencha todos os campos!");
      return;
    }

    const ordemDesejada = parseInt(formData.ordem, 10);

    const payload = {
      id: formData.id,
      nome: formData.nome,
      tipo: formData.tipo,
      ordem: ordemDesejada
    };

    try {
      if (isEditing) {
        // --- FLUXO DE EDIÇÃO (PUT) ---
        await etapaService.editar(formData.id, payload);
        alert("Etapa atualizada com sucesso!");
      } else {
        // --- FLUXO DE CRIAÇÃO (POST) ---
        await etapaService.cadastrar(payload);
        alert("Etapa criada com sucesso!");
      }

      // Recarrega tudo do banco para garantir que a ordem e os dados estão sincronizados
      await carregarEtapas();

      setShowModal(false);
      setFormData({ id: "", nome: "", ordem: "", tipo: "PADRAO" });

    } catch (error) {
      console.error("Erro ao salvar:", error);
      alert("Erro ao salvar etapa. Verifique o console.");
    }
  };

  const removeColumn = async (id) => {
    if (window.confirm("Tem certeza que deseja remover esta etapa permanentemente?")) {
      try {
        await etapaService.excluir(id);
        // Remove localmente para não precisar recarregar tudo
        setColumns(prev => prev.filter(col => col.id !== id));
      } catch (error) {
        alert("Erro ao excluir etapa.");
      }
    }
  };

  // Gera opções de posição (baseado no tamanho atual + 1 para novas inclusões)
  // Se for edição, usamos o tamanho atual. Se for criação, permitimos adicionar no fim.
  const totalSlots = isEditing ? columns.length - 1 : columns.length;
  const posicaoOptions = Array.from({ length: totalSlots - 1 }, (_, i) => i + 1);

  return (
    <section className="content-container" style={{ overflow: 'hidden', display: 'flex', flexDirection: 'column' }}>

      <div className="content-header">
        <div className="controls">
          {/* Botão chama handleOpenCreate agora */}
          <button className="btn-add-column" onClick={handleOpenCreate}>
            Adicionar coluna
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <line x1="12" y1="5" x2="12" y2="19"></line>
              <line x1="5" y1="12" x2="19" y2="12"></line>
            </svg>
          </button>
        </div>
      </div>

      <div className="kanban-container">
        {columns.map((col) => (
          <div key={col.id} className="kanban-column">
            <div className="column-header">

              {/* Ícone Esquerda: Lixeira (Excluir) */}
              <div className="header-icon">
                {!col.isFixed ? (
                  <svg onClick={() => removeColumn(col.id)} width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                    <polyline points="3 6 5 6 21 6"></polyline>
                    <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                  </svg>
                ) : (
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#777" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                    <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
                  </svg>
                )}
              </div>

              <span>{col.title}</span>

              {/* Ícone Direita: Lápis (Editar) */}
              <div className="header-icon">
                {!col.isFixed && (
                  // --- AQUI LIGAMOS O BOTÃO DE EDITAR ---
                  <svg
                    onClick={() => handleEditClick(col)}
                    width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"
                    style={{ cursor: 'pointer' }}
                  >
                    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                  </svg>
                )}
              </div>
            </div>
            <div className="column-body"></div>
          </div>
        ))}
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal">
            {/* Título dinâmico */}
            <h2>{isEditing ? "Editar Etapa" : "Nova Etapa"}</h2>

            <form onSubmit={handleSaveColumn}>

              <div className="form-group">
                <label>ID *</label>
                <input
                  className="form-input"
                  name="id"
                  value={formData.id}
                  onChange={handleInputChange}
                  placeholder="Ex: ET-005"
                  required
                  // Desabilitamos o ID na edição para evitar inconsistência com o Backend
                  // (Se você mudar o ID, vira outra entidade)
                  disabled={isEditing}
                  style={isEditing ? { backgroundColor: '#f0f0f0', cursor: 'not-allowed' } : {}}
                />
              </div>

              <div className="form-group">
                <label>Nome *</label>
                <input
                  className="form-input"
                  name="nome"
                  value={formData.nome}
                  onChange={handleInputChange}
                  placeholder="Ex: Corte"
                  required
                  autoFocus
                />
              </div>

              <div className="form-group">
                <label>Tipo *</label>
                <select
                  className="form-select"
                  name="tipo"
                  value={formData.tipo}
                  onChange={handleInputChange}
                >
                  <option value="PADRAO">Padrão</option>
                  <option value="PRODUCAO">Produção</option>
                  <option value="QUALIDADE">Qualidade</option>
                </select>
              </div>

              <div className="form-group">
                <label>Posição *</label>
                <select
                  className="form-select"
                  name="ordem"
                  value={formData.ordem}
                  onChange={handleInputChange}
                  required
                >
                  <option value="" disabled>Selecione a posição</option>
                  {posicaoOptions.map((pos) => (
                    <option key={pos} value={pos}>
                      Posição {pos}
                    </option>
                  ))}
                </select>
              </div>

              <div className="modal-actions">
                <button
                  type="button"
                  className="btn-cancel"
                  onClick={() => setShowModal(false)}
                >
                  Cancelar
                </button>
                <button type="submit" className="btn-save">
                  {isEditing ? "Salvar Alterações" : "Salvar"}
                </button>
              </div>

            </form>
          </div>
        </div>
      )}

    </section>
  );
}