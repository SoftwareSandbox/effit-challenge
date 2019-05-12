import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

type SnackColor = 'cyan' | 'red';

interface UpdateSnackCommand {
  message: string;
  color: SnackColor;
}

export default new Vuex.Store({
  state: {
    snack: '',
    snackColor: '',
  },
  mutations: {
    snack(state, snack: UpdateSnackCommand) {
      state.snack = snack.message || '';
      state.snackColor = snack.color || 'cyan';
    },
  },
  actions: {

  },
});
