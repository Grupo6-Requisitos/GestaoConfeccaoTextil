import React, { useEffect, useMemo, useState } from "react";
import FabricoCard from "../components/FabricoCard";
import CustomSelect from "../components/CustomSelect";
import api from "../services/api"; // existente no seu projeto; chamadas comentadas abaixo

// FILTERS simples (mesma UX das outras telas)
const FILTERS = [
  { value: "todos", label: "Todos" },
  { value: "nome", label: "Nome" },
  { value: "cnpj", label: "CNPJ" },
];

export default function Fabricos() {
  // toggle: quando chegar o backend ative (true) e descomente chamadas API.
  // const USE_API = true;
  const USE_API = false; // por enquanto in-memory

  const [query, setQuery] = useState("");
  const [filter, setFilter] = useState(FILTERS[0].value);

  const [fabricos, setFabricos] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  // UI state (modal/edit)
  const [modalOpen, setModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState("view"); // view | edit | create
  const [modalItem, setModalItem] = useState(null);
  const [saving, setSaving] = useState(false);
  const [deletingId, setDeletingId] = useState(null);

  // form local for create/edit
  const [form, setForm] = useState({ name: "", cnpj: "" });

  // ---------- In-memory seed (so page is usable immediately) ----------
  useEffect(() => {
    if (USE_API) return;
    // seed sample data (in-memory)
    const seed = [
      { id: 1, name: "Fábrica A", cnpj: "12.345.678/0001-90" },
      { id: 2, name: "Fábrica B", cnpj: "98.765.432/0001-10" },
      { id: 3, name: "Fábrica C", cnpj: "" },
    ];
    setFabricos(seed);
  }, [USE_API]);

  // ---------- Fetch (API) - commented but ready to use ----------
  useEffect(() => {
    if (!USE_API) return;

    let active = true;
    async function fetchAll() {
      try {
        setLoading(true);
        setError("");
        // const { data } = await api.get("/fabricos"); // <-- descomente quando backend existir
        // if (!active) return;
        // setFabricos((data || []).map((f) => normalize(f)));
      } catch (err) {
        console.error("Erro ao carregar fabricos", err);
        if (active) setError("Não foi possível carregar fábricas.");
      } finally {
        if (active) setLoading(false);
      }
    }
    fetchAll();
    return () => (active = false);
  }, [USE_API]);

  // ---------- Helpers ----------
  function normalize(raw, idx = 0) {
    return {
      id: raw.fab_id ?? raw.id ?? idx + 1,
      name: raw.fab_nf ?? raw.name ?? `Fábrica ${idx + 1}`,
      cnpj: raw.fab_cnpj ?? raw.cnpj ?? "",
    };
  }

  function nextId() {
    const max = fabricos.reduce((m, f) => Math.max(m, Number(f.id || 0)), 0);
    return max + 1;
  }

  // ---------- CRUD (in-memory implementations + commented API examples) ----------
  async function createFabrico(payload) {
    if (USE_API) {
      // API example:
      // const { data } = await api.post("/fabricos", { fab_nf: payload.name, fab_cnpj: payload.cnpj });
      // return normalize(data);
      return null;
    } else {
      // in-memory
      const newItem = { id: nextId(), name: payload.name, cnpj: payload.cnpj };
      setFabricos((prev) => [newItem, ...prev]);
      return newItem;
    }
  }

  async function updateFabrico(id, payload) {
    if (USE_API) {
      // API example:
      // const { data } = await api.put(`/fabricos/${id}`, { fab_nf: payload.name, fab_cnpj: payload.cnpj });
      // return normalize(data);
      return null;
    } else {
      setFabricos((prev) =>
        prev.map((f) => (f.id === id ? { ...f, ...payload } : f))
      );
      return { id, ...payload };
    }
  }

  async function deleteFabrico(id) {
    if (USE_API) {
      // API example:
      // await api.delete(`/fabricos/${id}`);
      return;
    } else {
      setFabricos((prev) => prev.filter((f) => f.id !== id));
    }
  }

  // ---------- UI handlers ----------
  const handleOpenCreate = () => {
    setModalMode("create");
    setForm({ name: "", cnpj: "" });
    setModalItem(null);
    setModalOpen(true);
  };

  const handleOpenView = (item) => {
    setModalMode("view");
    setModalItem(item);
    setForm({ name: item.name || "", cnpj: item.cnpj || "" });
    setModalOpen(true);
  };

  const handleStartEdit = () => {
    if (!modalItem) return;
    setModalMode("edit");
    setForm({ name: modalItem.name || "", cnpj: modalItem.cnpj || "" });
  };

  const handleCancel = () => {
    setModalOpen(false);
    setModalItem(null);
    setModalMode("view");
    setForm({ name: "", cnpj: "" });
  };

  const handleSubmit = async (e) => {
    e && e.preventDefault && e.preventDefault();
    setSaving(true);
    setError("");
    try {
      if (modalMode === "create") {
        const created = await createFabrico({
          name: form.name.trim(),
          cnpj: form.cnpj.trim(),
        });
        // if API returned object you might set created var accordingly
        // setFabricos((prev) => [created, ...prev]); // already done in in-memory path
        setModalItem(created);
        setModalMode("view");
      } else if (modalMode === "edit" && modalItem) {
        await updateFabrico(modalItem.id, {
          name: form.name.trim(),
          cnpj: form.cnpj.trim(),
        });
        // update local state already done in in-memory function
        const updated = {
          id: modalItem.id,
          name: form.name.trim(),
          cnpj: form.cnpj.trim(),
        };
        setModalItem(updated);
        setModalMode("view");
      }
    } catch (err) {
      console.error("Erro salvar fabrico", err);
      setError("Não foi possível salvar a fábrica.");
    } finally {
      setSaving(false);
    }
  };

  const handleConfirmDelete = async (id) => {
    const ok = window.confirm("Deseja realmente excluir este fabrico?");
    if (!ok) return;
    setDeletingId(id);
    try {
      await deleteFabrico(id);
      if (modalItem && modalItem.id === id) {
        handleCancel();
      }
    } catch (err) {
      console.error("Erro ao excluir fabrico", err);
      setError("Não foi possível excluir.");
    } finally {
      setDeletingId(null);
    }
  };

  // ---------- Filtering / sorting ----------
  const filtered = useMemo(() => {
    const q = (query || "").trim().toLowerCase();
    let list = fabricos.slice();

    if (q) {
      list = list.filter(
        (f) =>
          (f.name || "").toLowerCase().includes(q) ||
          (f.cnpj || "").toLowerCase().includes(q)
      );
    }

    if (filter === "nome") {
      list.sort((a, b) => (a.name || "").localeCompare(b.name || ""));
    } else if (filter === "cnpj") {
      list.sort((a, b) => (a.cnpj || "").localeCompare(b.cnpj || ""));
    }

    return list;
  }, [fabricos, query, filter]);

  // ---------- Render ----------
  return (
    <section className="content-container">
      <div className="content-header">
        <div className="title-wrap">
          <img
            src="/assets/factory24.svg"
            className="title-icon"
            alt="fábricas"
          />
          <h1>Fabrico</h1>
        </div>

        <div className="controls">
          <div style={{ display: "flex", gap: 8 }}>
            <button className="card-btn primary" onClick={handleOpenCreate}>
              Novo fabrico
            </button>
          </div>

          <div style={{ display: "flex", gap: 12, alignItems: "center" }}>
            <div className="search-box" role="search" style={{ minWidth: 240 }}>
              <input
                className="search-box__input"
                placeholder="Buscar..."
                value={query}
                onChange={(e) => setQuery(e.target.value)}
              />
            </div>

            <CustomSelect
              options={FILTERS}
              value={filter}
              onChange={(v) => setFilter(v)}
              placeholder="Ordenar"
            />
          </div>
        </div>
      </div>

      <div className="grid-products" style={{ minHeight: 160 }}>
        {loading && <div className="grid-feedback">Carregando fábricas...</div>}
        {error && !loading && (
          <div className="grid-feedback error">{error}</div>
        )}
        {!loading && !error && filtered.length === 0 && (
          <div className="grid-feedback">Nenhuma fábrica encontrada.</div>
        )}

        {!loading &&
          !error &&
          filtered.map((f) => (
            <FabricoCard
              key={f.id}
              fabrico={f}
              onClick={() => handleOpenView(f)}
            />
          ))}
      </div>

      {/* modal */}
      {modalOpen && (
        <div className="modal-overlay" onClick={handleCancel}>
          <div
            className="modal"
            role="dialog"
            aria-modal="true"
            onClick={(e) => e.stopPropagation()}
          >
            <div
              className="modal-header"
              style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
              }}
            >
              <div>
                <div className="modal-title">
                  {modalMode === "edit"
                    ? "Editar fabrico"
                    : "Detalhes do fabrico"}
                </div>
                <div className="modal-sub">
                  {modalItem ? `ID: ${modalItem.id}` : ""}
                </div>
              </div>

              <div style={{ display: "flex", gap: 8, alignItems: "center" }}>
                {modalMode === "view" && (
                  <>
                    <button
                      type="button"
                      className="icon-btn"
                      title="Editar"
                      onClick={handleStartEdit}
                    >
                      <svg
                        width="18"
                        height="18"
                        viewBox="0 0 24 24"
                        fill="none"
                        aria-hidden
                      >
                        <path
                          d="M3 21l3-1 11-11 2-3-3 2L8 19l-1 3z"
                          stroke="currentColor"
                          strokeWidth="1.4"
                          strokeLinecap="round"
                          strokeLinejoin="round"
                        />
                      </svg>
                    </button>
                    <button
                      type="button"
                      className="icon-btn danger"
                      title="Excluir"
                      onClick={() =>
                        modalItem && handleConfirmDelete(modalItem.id)
                      }
                    >
                      <svg
                        width="18"
                        height="18"
                        viewBox="0 0 24 24"
                        fill="none"
                        aria-hidden
                      >
                        <path
                          d="M3 6h18M8 6v12a2 2 0 0 0 2 2h4a2 2 0 0 0 2-2V6M10 6V4a2 2 0 0 1 2-2h0a2 2 0 0 1 2 2v2"
                          stroke="currentColor"
                          strokeWidth="1.4"
                          strokeLinecap="round"
                          strokeLinejoin="round"
                        />
                      </svg>
                    </button>
                  </>
                )}

                <button className="card-btn" onClick={handleCancel}>
                  Fechar
                </button>
              </div>
            </div>

            <div className="modal-body">
              {modalMode === "view" && modalItem && (
                <>
                  <div className="modal-field">
                    <span className="label">Nome fantasia</span>
                    <div className="value">{modalItem.name}</div>
                  </div>
                  <div className="modal-field">
                    <span className="label">CNPJ</span>
                    <div className="value">{modalItem.cnpj || "-"}</div>
                  </div>
                </>
              )}

              {modalMode === "edit" && (
                <form onSubmit={handleSubmit}>
                  <label className="field">
                    <span>Nome fantasia</span>
                    <input
                      required
                      value={form.name}
                      onChange={(e) =>
                        setForm((s) => ({ ...s, name: e.target.value }))
                      }
                    />
                  </label>

                  <label className="field">
                    <span>CNPJ</span>
                    <input
                      value={form.cnpj}
                      onChange={(e) =>
                        setForm((s) => ({ ...s, cnpj: e.target.value }))
                      }
                    />
                  </label>

                  <div style={{ display: "flex", gap: 8, marginTop: 12 }}>
                    <button
                      type="button"
                      className="card-btn"
                      onClick={() => {
                        setModalMode("view");
                      }}
                    >
                      Cancelar
                    </button>
                    <button
                      type="submit"
                      className="card-btn primary"
                      disabled={saving}
                    >
                      {saving ? "Salvando..." : "Salvar"}
                    </button>
                  </div>
                </form>
              )}

              {modalMode === "create" && (
                <form onSubmit={handleSubmit}>
                  <label className="field">
                    <span>Nome fantasia</span>
                    <input
                      required
                      value={form.name}
                      onChange={(e) =>
                        setForm((s) => ({ ...s, name: e.target.value }))
                      }
                    />
                  </label>

                  <label className="field">
                    <span>CNPJ</span>
                    <input
                      value={form.cnpj}
                      onChange={(e) =>
                        setForm((s) => ({ ...s, cnpj: e.target.value }))
                      }
                    />
                  </label>

                  <div style={{ display: "flex", gap: 8, marginTop: 12 }}>
                    <button
                      type="button"
                      className="card-btn"
                      onClick={handleCancel}
                    >
                      Cancelar
                    </button>
                    <button
                      type="submit"
                      className="card-btn primary"
                      disabled={saving}
                    >
                      {saving ? "Criando..." : "Criar"}
                    </button>
                  </div>
                </form>
              )}
            </div>
          </div>
        </div>
      )}
    </section>
  );
}
