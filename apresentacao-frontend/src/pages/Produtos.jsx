import React, { useState } from "react";
import ProductCard from "../components/ProductCard";
import CustomSelect from "../components/CustomSelect";
import producIcon from "../assets/products24.svg";

/* sample data (substituir por API depois) */
const sampleProducts = Array.from({ length: 12 }).map((_, i) => ({
  id: i + 1,
  name: `Modelo ${i + 1}`,
  type: i % 2 === 0 ? "Camisa" : "Vestido",
  createdAt: new Date(2025, 0, (i % 28) + 1),
  image: `/modelo.png`,
}));

const FILTERS = [
  { value: "mais-vendidos", label: "Mais vendidos" },
  { value: "data", label: "Data de criação" },
  { value: "nome", label: "Nome" },
];

export default function Produtos() {
  const [query, setQuery] = useState("");
  const [filter, setFilter] = useState(FILTERS[0].value);

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
                /* adicionar comportamento de busca */
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
        {sampleProducts
          .filter((p) => p.name.toLowerCase().includes(query.toLowerCase()))
          .map((p) => (
            <ProductCard key={p.id} product={p} />
          ))}
      </div>
    </section>
  );
}
