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
        snackbar: {
            snackShow: false,
            snackMessage: '',
            snackColor: 'cyan',
        },
        title: 'EFFIT',
    },
    mutations: {
        showSnackMessage(state, snack: UpdateSnackCommand) {
            state.snackbar.snackShow = !!snack.message;
            state.snackbar.snackMessage = snack.message || '';
            state.snackbar.snackColor = snack.color || 'cyan';
        },
        showSnackbar(state, updatedVisibility: boolean) {
            state.snackbar.snackShow = updatedVisibility;
        },
        updateTitle(state, currentViewTitle) {
            state.title = currentViewTitle;
        },
    },
    actions: {},
});
