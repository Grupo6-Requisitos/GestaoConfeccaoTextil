import React, { useEffect, useMemo, useState } from "react";
import ProductCard from "../components/ProductCard";
import CustomSelect from "../components/CustomSelect";
import producIcon from "../assets/products24.svg";
import api from "../services/api";

const FILTERS = [
  { value: "mais-vendidos", label: "Mais vendidos" },
  { value: "data", label: "Data de criação" },
  { value: "nome", label: "Nome" },
];

export default function Produtos() {
  const [query, setQuery] = useState("");
  const [filter, setFilter] = useState(FILTERS[0].value);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [editingId, setEditingId] = useState(null);
  const [saving, setSaving] = useState(false);
  const [deletingId, setDeletingId] = useState(null);
  const [detailLoadingId, setDetailLoadingId] = useState(null);
  const [form, setForm] = useState({ name: "", reference: "" });
  const [modalOpen, setModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState("view");
  const [modalProduct, setModalProduct] = useState(null);
  const [modalLoading, setModalLoading] = useState(false);

  const normalizeModelo = (modelo, idx = 0, insumosLoadedFlag) => {
    const insumosPadrao = Array.isArray(modelo.insumosPadrao)
      ? modelo.insumosPadrao
      : [];

    return {
      id: modelo.id || modelo.referencia || idx,
      name: modelo.nome || modelo.referencia || `Modelo ${idx + 1}`,
      reference: modelo.referencia,
      type: modelo.referencia ? `Ref. ${modelo.referencia}` : "Modelo",
      createdAt: modelo.criadoEm ? new Date(modelo.criadoEm) : null,
      imagemUrl: modelo.imagemUrl,
      insumosPadrao,
      insumosCount: insumosPadrao.length,
      image:
        modelo.imagemUrl && modelo.imagemUrl.trim()
          ? modelo.imagemUrl
          : "/modelo.png",
      insumosLoaded:
        typeof insumosLoadedFlag === "boolean"
          ? insumosLoadedFlag
          : Array.isArray(modelo.insumosPadrao),
    };
  };

  useEffect(() => {
    let active = true;

    async function fetchModelos() {
      try {
        setLoading(true);
        const { data } = await api.get("/modelos");
        if (!active) return;

        const normalized = (data || []).map(
          (modelo, idx) => normalizeModelo(modelo, idx, false) // força buscar detalhes no flip
        );

        setProducts(normalized);
        setError("");
      } catch (err) {
        console.error("Erro ao buscar modelos", err);
        if (active) {
          setError("Não foi possível carregar os modelos.");
        }
      } finally {
        if (active) {
          setLoading(false);
        }
      }
    }

    fetchModelos();
    return () => {
      active = false;
    };
  }, []);

  const fetchDetails = async (product) => {
    if (!product.reference) return;
    setDetailLoadingId(product.id);
    try {
      const { data } = await api.get(`/modelos/${product.reference}`);
      const updated = normalizeModelo({ ...product, ...data }, 0, true);
      setProducts((prev) =>
        prev.map((p) => (p.id === product.id ? updated : p))
      );
      return updated;
    } catch (err) {
      console.error("Erro ao carregar detalhes do modelo", err);
      setError("Não foi possível carregar os detalhes do modelo.");
    } finally {
      setDetailLoadingId(null);
    }
  };

  const handleDelete = async (product) => {
    const confirmed = window.confirm(
      `Deseja realmente excluir o modelo ${product.name}?`
    );
    if (!confirmed) return;

    setDeletingId(product.id);
    try {
      await api.delete(`/modelos/${product.reference}`);
      setProducts((prev) => prev.filter((p) => p.id !== product.id));
      if (editingId === product.id) setEditingId(null);
      if (modalProduct && modalProduct.id === product.id) {
        setModalOpen(false);
        setModalProduct(null);
      }
    } catch (err) {
      console.error("Erro ao excluir modelo", err);
      setError("Não foi possível excluir o modelo.");
    } finally {
      setDeletingId(null);
    }
  };

  const handleSave = async (evt) => {
    evt && evt.preventDefault && evt.preventDefault();
    const target = products.find((p) => p.id === editingId);
    if (!target) return;
    setSaving(true);
    try {
      const payload = {
        referencia: form.reference,
        nome: form.name,
        imagemUrl: target.imagemUrl || target.image || null,
        insumosPadrao: target.insumosPadrao || [],
      };

      const { data } = await api.put(`/modelos/${target.reference}`, payload);

      const updated = normalizeModelo(data || payload);
      setProducts((prev) =>
        prev.map((p) => (p.id === target.id ? updated : p))
      );
      setEditingId(null);
      setModalProduct(updated);
      setModalMode("view");
    } catch (err) {
      console.error("Erro ao salvar modelo", err);
      setError("Não foi possível salvar o modelo.");
    } finally {
      setSaving(false);
    }
  };

  const handleOpenModal = async (product) => {
    setModalOpen(true);
    setModalMode("view");
    setModalLoading(true);
    setModalProduct(product);
    try {
      const updated = await fetchDetails(product);
      if (updated) {
        setModalProduct(updated);
      }
    } finally {
      setModalLoading(false);
    }
  };

  const closeModal = () => {
    setModalOpen(false);
    setModalMode("view");
    setModalProduct(null);
    setEditingId(null);
  };

  const filteredProducts = useMemo(() => {
    const byName = products.filter((p) =>
      p.name.toLowerCase().includes(query.toLowerCase())
    );

    if (filter === "nome") {
      return byName.slice().sort((a, b) => a.name.localeCompare(b.name));
    }
    if (filter === "data") {
      return byName.slice().sort((a, b) => {
        const aTime = a.createdAt ? new Date(a.createdAt).getTime() : 0;
        const bTime = b.createdAt ? new Date(b.createdAt).getTime() : 0;
        return bTime - aTime;
      });
    }

    return byName;
  }, [products, query, filter]);

  return (
    <section className="content-container">
      <div className="content-header">
        <div className="title-wrap">
          {/* agora usamos o SVG 'producs24.svg' da pasta assets */}
          <img src={producIcon} alt="ícone produtos" className="title-icon" />
          <h1>Produtos</h1>
        </div>

        <div className="controls">
          {/* search box: input + icon inside */}
          <div className="search-box" role="search">
            <input
              className="search-box__input"
              placeholder="Buscar..."
              aria-label="Buscar produtos"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
            />
            <button
              type="button"
              className="search-box__icon"
              aria-label="Buscar"
              onClick={() => {
                /* comportamento de busca caso queira */
              }}
            >
              <svg
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                aria-hidden
              >
                <path
                  d="M21 21l-4.35-4.35"
                  stroke="currentColor"
                  strokeWidth="1.6"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
                <circle
                  cx="11"
                  cy="11"
                  r="6"
                  stroke="currentColor"
                  strokeWidth="1.6"
                />
              </svg>
            </button>
          </div>

          <CustomSelect
            options={FILTERS}
            value={filter}
            onChange={(v) => setFilter(v)}
            placeholder="Filtrar"
          />
        </div>
      </div>

      <div className="grid-products">
        {loading && <div className="grid-feedback">Carregando modelos...</div>}
        {error && !loading && (
          <div className="grid-feedback error">{error}</div>
        )}
        {!loading && !error && filteredProducts.length === 0 && (
          <div className="grid-feedback">Nenhum modelo encontrado.</div>
        )}
        {!loading &&
          !error &&
          filteredProducts.map((p) => (
            <ProductCard
              key={p.id}
              product={p}
              onFlip={() => handleOpenModal(p)} // open modal when card clicked
              onEdit={() => {
                /* unused: editing occurs inside modal now */
              }}
              saving={saving}
              onDelete={() => handleDelete(p)}
              deleting={deletingId === p.id}
            />
          ))}
      </div>

      {modalOpen && (
        <div className="modal-overlay" onClick={closeModal}>
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
                    ? "Editar modelo"
                    : "Detalhes do modelo"}
                </div>
                <div className="modal-sub">
                  {modalProduct?.reference
                    ? `Ref. ${modalProduct.reference}`
                    : "Sem referência"}
                </div>
              </div>

              <div style={{ display: "flex", gap: 8, alignItems: "center" }}>
                {/* Edit icon: toggles edit mode and initializes form */}
                <button
                  type="button"
                  title={modalMode === "edit" ? "Cancelar edição" : "Editar"}
                  className="icon-btn"
                  onClick={() => {
                    if (!modalProduct) return;
                    if (modalMode !== "edit") {
                      setModalMode("edit");
                      setEditingId(modalProduct.id);
                      setForm({
                        name: modalProduct.name || "",
                        reference: modalProduct.reference || "",
                      });
                    } else {
                      setModalMode("view");
                      setEditingId(null);
                    }
                  }}
                >
                  {/* pencil icon */}
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

                {/* Delete icon: confirms and deletes */}
                <button
                  type="button"
                  title="Excluir"
                  className="icon-btn danger"
                  onClick={() => {
                    if (!modalProduct) return;
                    const ok = window.confirm(
                      `Deseja realmente excluir o modelo ${modalProduct.name}?`
                    );
                    if (!ok) return;
                    handleDelete(modalProduct);
                  }}
                >
                  {/* trash icon */}
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

                <button className="card-btn" onClick={closeModal}>
                  Fechar
                </button>
              </div>
            </div>

            {modalLoading && (
              <div className="details-empty">Carregando detalhes...</div>
            )}

            {!modalLoading && modalProduct && modalMode === "view" && (
              <div className="modal-body">
                <div className="modal-field">
                  <span className="label">Nome</span>
                  <div className="value">{modalProduct.name}</div>
                </div>
                <div className="modal-field">
                  <span className="label">Referência</span>
                  <div className="value">{modalProduct.reference || "-"}</div>
                </div>
                <div className="modal-field">
                  <span className="label">Insumos padrão</span>
                  {modalProduct.insumosLoaded ? (
                    modalProduct.insumosPadrao &&
                    modalProduct.insumosPadrao.length > 0 ? (
                      <ul className="details-insumos-list">
                        {modalProduct.insumosPadrao.map((insumo, idx) => (
                          <li key={`${insumo.insumoId || idx}`}>
                            <span className="insumo-id">
                              {insumo.insumoId || "Insumo"}
                            </span>
                            <span className="insumo-qty">
                              {insumo.quantidadeSugerida} un
                            </span>
                          </li>
                        ))}
                      </ul>
                    ) : (
                      <div className="details-empty">
                        Nenhum insumo cadastrado.
                      </div>
                    )
                  ) : (
                    <div className="details-empty">
                      Detalhes ainda não carregados.
                    </div>
                  )}
                </div>
              </div>
            )}

            {!modalLoading && modalMode === "edit" && modalProduct && (
              <form className="modal-body" onSubmit={handleSave}>
                <label className="field">
                  <span>Nome</span>
                  <input
                    value={form.name}
                    onChange={(e) =>
                      setForm((f) => ({ ...f, name: e.target.value }))
                    }
                    required
                  />
                </label>
                <label className="field">
                  <span>Referência</span>
                  <input
                    value={form.reference}
                    onChange={(e) =>
                      setForm((f) => ({ ...f, reference: e.target.value }))
                    }
                    required
                  />
                </label>

                <div className="modal-field">
                  <span className="label">Insumos padrão</span>
                  {modalProduct.insumosLoaded ? (
                    modalProduct.insumosPadrao &&
                    modalProduct.insumosPadrao.length > 0 ? (
                      <ul className="details-insumos-list">
                        {modalProduct.insumosPadrao.map((insumo, idx) => (
                          <li key={`${insumo.insumoId || idx}`}>
                            <span className="insumo-id">
                              {insumo.insumoId || "Insumo"}
                            </span>
                            <span className="insumo-qty">
                              {insumo.quantidadeSugerida} un
                            </span>
                          </li>
                        ))}
                      </ul>
                    ) : (
                      <div className="details-empty">
                        Nenhum insumo cadastrado.
                      </div>
                    )
                  ) : (
                    <div className="details-empty">
                      Detalhes ainda não carregados.
                    </div>
                  )}
                </div>

                <div className="details-actions">
                  <button
                    type="button"
                    className="card-btn"
                    onClick={() => {
                      setModalMode("view");
                      setEditingId(null);
                    }}
                    disabled={saving}
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
          </div>
        </div>
      )}
    </section>
  );
}
