// import React from "react";
// import { Outlet } from "react-router-dom";
// import Sidebar from "../components/Sidebar";
// import Header from "../components/Header";
// import Footer from "../components/Footer";

// // Layout revisado:
// // - Header ocupa 100% da largura no topo.
// // - Abaixo do header há um container (shell-body) com sidebar à esquerda e conteúdo à direita.
// // - Footer aparece na base (única instância).
// export default function MainLayout() {
//   return (
//     <div className="app-shell">
//       <Header />

//       <div className="shell-body">
//         <aside className="sidebar-wrapper">
//           <Sidebar />
//         </aside>

//         <main className="main-area">
//           <Outlet />
//         </main>
//       </div>

//       <Footer />
//     </div>
//   );
// }

import React from "react";
import { Outlet } from "react-router-dom";
import Sidebar from "../components/Sidebar";
import Header from "../components/Header";
import Footer from "../components/Footer";

/*
  Novo layout em duas colunas:
  - coluna esquerda: Sidebar (sempre do topo ao rodapé)
  - coluna direita: Header (topo) + conteúdo (Outlet) + Footer (embaixo)
*/
export default function MainLayout() {
  return (
    <div className="app-shell">
      {/* coluna esquerda: sidebar */}
      <aside className="sidebar-column">
        <Sidebar />
      </aside>

      {/* coluna direita: header + conteúdo + footer */}
      <div className="content-column">
        <Header />
        <main className="content-area">
          <Outlet />
        </main>
        <Footer />
      </div>
    </div>
  );
}
