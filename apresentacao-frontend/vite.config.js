import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      // encaminha /api para o backend
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        secure: false,
        // remove o prefixo /api antes de chegar no backend Spring (/modelos, etc)
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
    },
  },
});
