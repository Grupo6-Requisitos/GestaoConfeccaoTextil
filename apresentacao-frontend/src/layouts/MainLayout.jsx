import React from "react";
import { Outlet } from "react-router-dom";
import Header from "../components/Header";
import Footer from "../components/Footer";

export default function MainLayout() {
  return (
    <div className="app-root">
      <Header />
      <main style={{ padding: "1rem" }}>
        <Outlet />
      </main>
      <Footer />
    </div>
  );
}
