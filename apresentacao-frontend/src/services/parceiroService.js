import api from './api';

const ENDPOINT = '/parceiros';

export const parceiroService = {
  listar: async () => {
    try {
      const response = await api.get(ENDPOINT);
      return response.data;
    } catch (error) {
      console.error("Erro ao listar parceiros", error);
      return [];
    }
  },

  cadastrar: async (dados) => {
    const response = await api.post(ENDPOINT, dados);
    return response.data;
  },

  excluir: async (id) => {
    await api.delete(`${ENDPOINT}/${id}`);
  }
};