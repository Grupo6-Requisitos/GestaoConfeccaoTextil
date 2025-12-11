// import React from "react";

// function formatDate(d) {
//   const dd = String(d.getDate()).padStart(2, "0");
//   const mm = String(d.getMonth() + 1).padStart(2, "0");
//   const yyyy = d.getFullYear();
//   return `${dd}/${mm}/${yyyy}`;
// }

// // ProductCard expects product { id, name, type, createdAt: Date, image }
// export default function ProductCard({ product }) {
//   return (
//     <article
//       className="product-card"
//       role="article"
//       aria-labelledby={`product-${product.id}`}
//     >
//       <div
//         className="card-media"
//         style={{
//           /*
//             Gradient changed per request:
//             - no overlay at the top (transparent)
//             - subtle blue near the bottom (use low alpha for subtlety)
//             - the URL image sits below the gradient layer
//           */
//           backgroundImage: `linear-gradient(to bottom, rgba(169,226,242,0) 60%, rgba(169,226,242,0.14) 100%), url(${product.image})`,
//         }}
//       >
//         {/* name aligned left, bigger and moved a bit lower */}
//         <div className="card-name" id={`product-${product.id}`}>
//           <svg
//             width="16"
//             height="16"
//             viewBox="0 0 24 24"
//             fill="none"
//             aria-hidden
//           >
//             <path
//               d="M3 7v13h13V7"
//               stroke="currentColor"
//               strokeWidth="1.2"
//               strokeLinecap="round"
//               strokeLinejoin="round"
//             />
//             <path
//               d="M7 3h8v4H7z"
//               stroke="currentColor"
//               strokeWidth="1.2"
//               strokeLinecap="round"
//               strokeLinejoin="round"
//             />
//           </svg>
//           <span className="card-name__text">{product.name}</span>
//         </div>
//       </div>

//       {/* bottom area: pure gray background (no gradient) - colors controlled via CSS variables */}
//       <div className="card-meta">
//         <div className="type">{product.type}</div>
//         <div className="created">Criado em {formatDate(product.createdAt)}</div>
//       </div>
//     </article>
//   );
// }

import React from "react";
import tagIcon from "../assets/tag.svg"; // <- novo: coloque src/assets/tag.svg no projeto

function formatDate(d) {
  const dd = String(d.getDate()).padStart(2, "0");
  const mm = String(d.getMonth() + 1).padStart(2, "0");
  const yyyy = d.getFullYear();
  return `${dd}/${mm}/${yyyy}`;
}

// ProductCard expects product { id, name, type, createdAt: Date, image }
export default function ProductCard({ product }) {
  return (
    <article
      className="product-card"
      role="article"
      aria-labelledby={`product-${product.id}`}
    >
      <div
        className="card-media"
        style={{
          /*
            Degradê (subtle blue at bottom):
            - Para ajustar a intensidade/alcance do azul edite esta linha:
              - aumentar o alpha (0.14) para um valor maior (ex: 0.25) deixa o azul mais forte;
              - reduzir o primeiro percentual (60%) para um valor menor (ex: 40%) fará o azul começar mais acima.
            Exemplo para mais azul: replace `rgba(169,226,242,0.14)` por `rgba(169,226,242,0.25)` ou
            use `linear-gradient(to bottom, rgba(169,226,242,0) 40%, rgba(169,226,242,0.25) 100%), ...`
          */
          backgroundImage: `linear-gradient(to bottom, rgba(169,226,242,0) 60%, rgba(169,226,242,0.75) 100%), url(${product.image})`,
        }}
      >
        {/* name aligned left, bigger and moved a bit lower */}
        <div className="card-name" id={`product-${product.id}`}>
          {/* agora usamos o SVG externo (tag.svg) vindo de src/assets */}
          <img
            src={tagIcon}
            alt="etiqueta"
            className="card-name__icon"
            width="16"
            height="16"
          />
          <span className="card-name__text">{product.name}</span>
        </div>
      </div>

      {/* bottom area: pure gray background (no gradient) - colors controlled via CSS variables */}
      <div className="card-meta">
        <div className="type">{product.type}</div>
        <div className="created">Criado em {formatDate(product.createdAt)}</div>
      </div>
    </article>
  );
}
