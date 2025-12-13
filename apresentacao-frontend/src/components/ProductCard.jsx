import React from "react";
import tagIcon from "../assets/tag.svg"; // <- certifique-se que src/assets/tag.svg existe

function formatDate(value) {
  if (!value) return "sem data";
  const dateObj = value instanceof Date ? value : new Date(value);
  if (Number.isNaN(dateObj.getTime())) return "sem data";

  const dd = String(dateObj.getDate()).padStart(2, "0");
  const mm = String(dateObj.getMonth() + 1).padStart(2, "0");
  const yyyy = dateObj.getFullYear();
  return `${dd}/${mm}/${yyyy}`;
}

// ProductCard expects product { id, name, image, reference?, insumosCount?, createdAt?, insumosPadrao? }
export default function ProductCard({
  product,
  onFlip,
  onEdit,
  saving,
  onDelete,
  deleting,
}) {
  const insumoInfo =
    typeof product.insumosCount === "number"
      ? `${product.insumosCount} insumo${
          product.insumosCount === 1 ? "" : "s"
        } padrão`
      : null;

  return (
    <div className="card-wrapper">
      <article
        className="product-card"
        role="article"
        aria-labelledby={`product-${product.id}`}
        onClick={() => onFlip?.(product)} // clicking anywhere on the card opens modal
        style={{ cursor: "pointer" }}
      >
        <div className="card-face card-front">
          <div
            className="card-media"
            style={{
              /*
                Degradê sutil restaurado (igual ao anterior):
                - começa transparente e no 60% aparece o azul com alpha 0.14
                - aumente o alpha ou reduza 60% para deixar azul mais forte / mais acima
              */
              backgroundImage: `linear-gradient(to bottom, rgba(169,226,242,0) 60%, rgba(169,226,242,0.14) 100%), url(${product.image})`,
            }}
            aria-hidden
          >
            {/* Title + icon moved to bottom-left (no background) */}
            <div
              className="card-name"
              id={`product-${product.id}`}
              style={{
                left: 16,
                bottom: 64,
                position: "absolute",
              }}
            >
              <img
                src={tagIcon}
                alt="etiqueta"
                className="card-name__icon"
                width="16"
                height="16"
                style={{ display: "inline-block" }}
              />
              <span className="card-name__text">{product.name}</span>
            </div>

            {/* Note: removed the 'Ver detalhes' button and any other card-level action buttons */}
          </div>

          <div className="card-meta">
            <div className="type">{product.type || "Modelo"}</div>
            <div className="created">
              {insumoInfo ||
                (product.createdAt
                  ? `Criado em ${formatDate(product.createdAt)}`
                  : "Detalhes não informados")}
            </div>
          </div>
        </div>
      </article>
    </div>
  );
}
