import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    snack: '',
  },
  mutations: {
    snack(state, snack) {
      state.snack = snack;
    },
  },
  actions: {

  },
});
