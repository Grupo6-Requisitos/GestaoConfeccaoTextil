import api from './api';

// Backend controller is mounted at /api/parceiros
const ENDPOINT = '/api/parceiros';

export const parceiroService = {
  listar: async () => {
    const response = await api.get(ENDPOINT);
    return response.data;
  },

  cadastrar: async (dados) => {
    const response = await api.post(ENDPOINT, dados);
    return response.data;
  },

  editar: async (id, dados) => {
    const response = await api.put(`${ENDPOINT}/${id}`, dados);
    return response.data;
  },

  excluir: async (id) => {
    await api.delete(`${ENDPOINT}/${id}`);
  }
};
