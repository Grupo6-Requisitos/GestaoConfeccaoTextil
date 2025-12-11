import React from "react";
import { Link } from "react-router-dom";
import logo from "../assets/logo.png";

export default function Header() {
  return (
    <header style={{ padding: "1rem", borderBottom: "1px solid #eee" }}>
      <div style={{ display: "flex", alignItems: "center", gap: "1rem" }}>
        <img src={logo} alt="logo" style={{ height: 36 }} />
        <nav>
          <Link to="/">Home</Link> {" | "}
          <Link to="/about">Sobre</Link>
        </nav>
      </div>
    </header>
  );
}
