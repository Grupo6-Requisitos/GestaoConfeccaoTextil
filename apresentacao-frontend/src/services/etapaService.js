import api from './api';

// Usamos o caminho completo do backend para alinhar com o controlador (@RequestMapping("/api/etapas")).
const ENDPOINT = '/api/etapas';

export const etapaService = {
  listar: async () => {
    const response = await api.get(ENDPOINT);
    return response.data;
  },

  cadastrar: async (dados) => {
    const response = await api.post(ENDPOINT, dados);
    return response.data;
  },

  // --- NOVA FUNÇÃO DE EDITAR ---
    editar: async (id, dados) => {
      const response = await api.put(`${ENDPOINT}/${id}`, dados);
      return response.data;
    },

  excluir: async (id) => {
    await api.delete(`${ENDPOINT}/${id}`);
  }
};
