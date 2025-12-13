import React from "react";
import { NavLink } from "react-router-dom";

// Menu atualizado com FABRICOS
const menu = [
  { label: "Início", path: "/" },
  { label: "Pedidos", path: "/pedidos" },
  { label: "Facções", path: "/faccao" },
  { label: "Clientes", path: "/clientes" },
  { label: "Produtos", path: "/produtos" },
  { label: "Fabricos", path: "/fabricos" },
  { label: "Estoque", path: "/estoque" },
  { label: "Etapas", path: "/etapas" },
  { label: "Parceiros", path: "/parceiros" },
];

const Icon = ({ name }) => {
  switch (name) {
    case "home":
      return (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
          <path
            d="M3 11l9-7 9 7v8a1 1 0 0 1-1 1h-5v-6H9v6H4a1 1 0 0 1-1-1v-8z"
            stroke="currentColor"
            strokeWidth="1.4"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
        </svg>
      );

    case "orders":
      return (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
          <path
            d="M3 6h18M8 6v12M16 6v12M3 18h18"
            stroke="currentColor"
            strokeWidth="1.4"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
        </svg>
      );

    case "products":
      return (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
          <rect
            x="3"
            y="3"
            width="18"
            height="18"
            rx="4"
            stroke="currentColor"
            strokeWidth="1.4"
          />
          <path d="M3 9h18" stroke="currentColor" strokeWidth="1.4" />
        </svg>
      );

    case "clients":
      return (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
          <circle
            cx="12"
            cy="8"
            r="3"
            stroke="currentColor"
            strokeWidth="1.4"
          />
          <path
            d="M6 20c1.5-4 10.5-4 12 0"
            stroke="currentColor"
            strokeWidth="1.4"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
        </svg>
      );

    case "stock":
      return (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
          <path
            d="M3 7l9-4 9 4v10l-9 4-9-4V7z"
            stroke="currentColor"
            strokeWidth="1.4"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
        </svg>
      );

    case "stages":
      return (
        <svg
          width="18"
          height="18"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="1.4"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <rect x="3" y="3" width="18" height="18" rx="2" />
          <line x1="9" y1="3" x2="9" y2="21" />
          <line x1="15" y1="3" x2="15" y2="21" />
        </svg>
      );

    case "partners":
    case "fabricos": // <-- reutilizado para Fabricos
      return (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
          <path
            d="M3 21v-4a4 4 0 0 1 4-4h10a4 4 0 0 1 4 4v4"
            stroke="currentColor"
            strokeWidth="1.4"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
          <circle
            cx="12"
            cy="7"
            r="4"
            stroke="currentColor"
            strokeWidth="1.4"
          />
        </svg>
      );

    default:
      return (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
          <circle
            cx="12"
            cy="12"
            r="8"
            stroke="currentColor"
            strokeWidth="1.4"
          />
        </svg>
      );
  }
};

export default function Sidebar() {
  return (
    <div className="sidebar">
      <div className="logo-area">
        <img src="/logo.png" alt="logo" className="sidebar-logo" />
      </div>

      <div className="sidebar-top">
        <NavLink to="/novo-pedido" className="btn-new-order">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
            <path
              d="M12 5v14M5 12h14"
              stroke="currentColor"
              strokeWidth="1.6"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
          <span className="btn-text">Novo pedido</span>
        </NavLink>
      </div>

      <nav className="menu" aria-label="Principal">
        {menu.map((item) => {
          const iconName =
            item.label === "Início"
              ? "home"
              : item.label === "Pedidos"
              ? "orders"
              : item.label === "Clientes"
              ? "clients"
              : item.label === "Produtos"
              ? "products"
              : item.label === "Fabricos"
              ? "fabricos"
              : item.label === "Estoque"
              ? "stock"
              : item.label === "Etapas"
              ? "stages"
              : item.label === "Parceiros"
              ? "partners"
              : "settings";

          return (
            <NavLink
              key={item.label}
              to={item.path}
              end={item.path === "/"}
              className={({ isActive }) =>
                `menu-item ${isActive ? "active" : ""}`
              }
            >
              <div className="icon">
                <Icon name={iconName} />
              </div>
              <div className="label">{item.label}</div>
            </NavLink>
          );
        })}
      </nav>
    </div>
  );
}
