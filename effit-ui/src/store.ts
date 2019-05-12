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
    snackShow: false,
    snackMessage: '',
    snackColor: '',
  },
  mutations: {
    showSnackMessage(state, snack: UpdateSnackCommand) {
      state.snackShow = !!snack.message;
      state.snackMessage = snack.message || '';
      state.snackColor = snack.color || 'cyan';
    },
    showSnackbar(state, updatedVisibility: boolean) {
      state.snackShow = updatedVisibility;
    }
  },
  actions: {

  },
});
