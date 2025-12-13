import React from "react";
import { Routes, Route } from "react-router-dom";
import MainLayout from "./layouts/MainLayout";
import Produtos from "./pages/Produtos";
import Parceiros from "./pages/Parceiros";
import Etapas from "./pages/Etapas"; // <--- Importar

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
      <Route path="/" element={<MainLayout />}>
        <Route index element={<Home />} />
        <Route path="produtos" element={<Produtos />} />
        <Route path="parceiros" element={<Parceiros />} />
        <Route path="etapas" element={<Etapas />} />
      </Route>
    </Routes>
  );
}