import React from "react";
import { NavLink } from "react-router-dom";

const menu = [
  { label: "Início", path: "/" },
  { label: "Pedidos", path: "/pedidos" },
  { label: "Facções", path: "/faccao" },
  { label: "Clientes", path: "/clientes" },
  { label: "Produtos", path: "/produtos" },
  { label: "Estoque", path: "/estoque" },
  { label: "Financeiro", path: "/financeiro" },
  { label: "Configurações", path: "/config" },
];

const Icon = ({ name }) => {
  // keep small icons that inherit currentColor
  switch (name) {
    case "home":
      return (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" aria-hidden>
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
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" aria-hidden>
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
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" aria-hidden>
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
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" aria-hidden>
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
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" aria-hidden>
          <path
            d="M3 7l9-4 9 4v10l-9 4-9-4V7z"
            stroke="currentColor"
            strokeWidth="1.4"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
        </svg>
      );
    case "finance":
      return (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" aria-hidden>
          <path
            d="M12 1v22M5 6h14M5 18h14"
            stroke="currentColor"
            strokeWidth="1.4"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
        </svg>
      );
    default:
      return (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" aria-hidden>
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
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            aria-hidden
          >
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
              : item.label === "Facções"
              ? "settings"
              : item.label === "Clientes"
              ? "clients"
              : item.label === "Produtos"
              ? "products"
              : item.label === "Estoque"
              ? "stock"
              : item.label === "Financeiro"
              ? "finance"
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
