<template>
    <v-snackbar
            v-model="showSnackbar"
            top
            :color="color"
            :timeout="6000"
            :vertical="false"
            :multi-line="true"
    >
        {{ snackbarMessage }}
        <v-btn dark flat @click="performUndo">Undo</v-btn>
        <v-btn dark flat @click="closeSnackbar()">Close</v-btn>
    </v-snackbar>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';

    @Component({
        components: {},
    })
    export default class BaseSnackBar extends Vue {

        get color() {
            return this.$store.state.snackbar.snackColor;
        }

        get snackbarMessage() {
            return this.$store.state.snackbar.snackMessage;
        }

        get showSnackbar() {
            return this.$store.state.snackbar.snackShow;
        }

        set showSnackbar(bool: boolean) {
            this.$store.commit('showSnackbar', bool);
        }

        get undoFunctionWasPassed() {
            return !!this.$store.state.snackbar.undo;
        }

        private closeSnackbar() {
            this.$store.commit('showSnackbar', false);
        }

        private performUndo() {
            this.$store.state.snackbar.undo();
            this.closeSnackbar();
        }
    }
</script>