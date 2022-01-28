
import configs from '../Config.json';
import axios from 'axios';

export const createGameService = () => {
    return axios.post(`${configs.baseUrl}/mancala/newgame`);
}

export const gameActionService = (gameId, pitId) => {
    return axios.put(`${configs.baseUrl}/mancala/${gameId}/pits/${pitId}`).catch(function (error) {
        return error.response;
   });
}