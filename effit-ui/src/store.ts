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
    snackColor: 'cyan',
    title: 'EFFIT',
  },
  mutations: {
    showSnackMessage(state, snack: UpdateSnackCommand) {
      state.snackShow = !!snack.message;
      state.snackMessage = snack.message || '';
      state.snackColor = snack.color || 'cyan';
    },
    showSnackbar(state, updatedVisibility: boolean) {
      state.snackShow = updatedVisibility;
    },
    routerViewWasSwitched(state, currentViewTitle) {
      state.title = currentViewTitle;
    },
  },
  actions: {

  },
});
