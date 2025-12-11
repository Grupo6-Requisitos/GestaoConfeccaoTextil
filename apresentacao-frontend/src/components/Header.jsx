import React from "react";

/*
  Header component:
  - height controlled by CSS variable --header-height
  - user-block contains texts at left and avatar at right (texts left-aligned)
  - everything vertically centered and responsive
*/
export default function Header({
  userName = "Mateus Marinho",
  userRole = "Gerente",
}) {
  return (
    <header className="header" role="banner">
      {/* left area intentionally empty (logo sits in sidebar) */}
      <div className="header-left" />

      {/* right area: user block with texts (left) + avatar (right) */}
      <div className="header-right" aria-label="Informações do usuário">
        <div
          className="user-block"
          style={{ display: "flex", alignItems: "center", gap: "12px" }}
        >
          <div
            className="user-text"
            style={{
              display: "flex",
              flexDirection: "column",
              alignItems: "flex-start",
              lineHeight: 1,
            }}
          >
            <div
              className="user-name"
              style={{ fontWeight: 500, color: "#404040", fontSize: 16 }}
            >
              {userName}
            </div>
            <div
              className="user-role"
              style={{ fontSize: 16, color: "#7B7D80" }}
            >
              {userRole}
            </div>
          </div>

          <div
            className="avatar"
            aria-hidden
            style={{
              width: 44,
              height: 44,
              borderRadius: "50%",
              background: "#D9EEF3",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              fontWeight: 700,
              color: "var(--muted-dark)",
              flex: "0 0 auto",
            }}
          >
            MM
          </div>
        </div>
      </div>
    </header>
  );
}
