import api from './api';

// --- CORREÇÃO AQUI: Remova o "/api" do início ---
// O api.js já coloca o prefixo, então aqui deixamos apenas o recurso final.
const ENDPOINT = '/etapas';
// ------------------------------------------------

export const etapaService = {
  listar: async () => {
    try {
      console.log("[JS] 🔍 Buscando lista de etapas...");
      const response = await api.get(ENDPOINT);
      return response.data;
    } catch (error) {
      console.error("[JS] ❌ Erro ao listar etapas", error);
      return [];
    }
  },

  cadastrar: async (dados) => {
    console.log("[JS] 🚀 Enviando Payload para o Backend:", JSON.stringify(dados, null, 2));

    try {
      // O Axios vai transformar isso em: http://localhost:8080/api/etapas
      const response = await api.post(ENDPOINT, dados);
      console.log("[JS] ✅ Sucesso! Resposta do Backend:", response.data);
      return response.data;
    } catch (error) {
      if (error.response) {
        console.error("[JS] ❌ Erro do Servidor:", error.response.status, error.response.data);
      } else {
        console.error("[JS] ❌ Erro de Rede/Código:", error.message);
      }
      throw error;
    }
  },

  // --- NOVA FUNÇÃO DE EDITAR ---
    editar: async (id, dados) => {
      console.log(`[JS] 📝 Editando ID ${id} (PUT):`, JSON.stringify(dados, null, 2));
      try {
        // Chama PUT /etapas/{id}
        const response = await api.put(`${ENDPOINT}/${id}`, dados);
        return response.data;
      } catch (error) {
        console.error("[JS] ❌ Erro ao editar:", error);
        throw error;
      }
    },

  excluir: async (id) => {
    console.log(`[JS] 🗑️ Excluindo etapa ID: ${id}`);
    await api.delete(`${ENDPOINT}/${id}`);
  }
};