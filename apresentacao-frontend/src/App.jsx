import React from "react";
import { Routes, Route } from "react-router-dom";
import MainLayout from "./layouts/MainLayout";
import Produtos from "./pages/Produtos";

// Simple placeholder page for root (you can replace later)
function Home() {
  return (
    <div style={{ padding: 24 }}>
      <h2>Início</h2>
      <p>Página inicial (placeholder).</p>
    </div>
  );
}

export default function App() {
  return (
    <Routes>
      {/* All pages that share header/sidebar/footer go inside MainLayout via Outlet */}
      <Route path="/" element={<MainLayout />}>
        <Route index element={<Home />} />
        <Route path="produtos" element={<Produtos />} />
        {/* outras rotas que usam o mesmo layout: pedidos, clientes etc */}
      </Route>

      {/* Rotas públicas/sem layout (login, landing) podem ficar fora */}
    </Routes>
  );
}
