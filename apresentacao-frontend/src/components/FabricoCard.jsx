import React from "react";

/*
  FabricoCard
  - simples visual para cada fabrico (reaproveita estilos do projeto)
  - props:
    - fabrico: { id, name, cnpj }
    - onClick (abrir modal/view)
*/
export default function FabricoCard({ fabrico, onClick }) {
  return (
    <article
      className="product-card"
      role="article"
      aria-labelledby={`fabrico-${fabrico.id}`}
      onClick={() => onClick && onClick(fabrico)}
      style={{ cursor: "pointer" }}
    >
      <div
        className="card-media"
        style={{
          /* leve gradiente igual ao dos cards já existentes */
          backgroundImage: `linear-gradient(to bottom, rgba(169,226,242,0) 60%, rgba(169,226,242,0.14) 100%), url(/fabrico-bg.jpg)`,
        }}
      >
        <div
          id={`fabrico-${fabrico.id}`}
          className="card-name"
          style={{ left: 16, bottom: 64 }}
        >
          {/* sem ícone específico aqui; mantém layout semelhante */}
          <span className="card-name__text">{fabrico.name}</span>
        </div>
      </div>

      <div
        className="card-meta"
        style={{
          background: "var(--card-bottom-bg)",
          color: "var(--card-bottom-text)",
        }}
      >
        <div className="type">
          {fabrico.cnpj ? `CNPJ: ${fabrico.cnpj}` : "CNPJ não informado"}
        </div>
        <div className="created">{`ID: ${fabrico.id}`}</div>
      </div>
    </article>
  );
}
