<template>
    <v-snackbar
            v-model="showSnackbar"
            top
            color="cyan darken-2"
            :timeout="3000"
    >
        {{ snackbarMessage }}
        <v-btn dark flat @click="closeSnackbar()">Close</v-btn>
    </v-snackbar>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';

    @Component({
        components: {},
    })
    export default class BaseSnackBar extends Vue {

        get snackbarMessage() {
            return this.$store.state.snack;
        }

        get showSnackbar() {
            return !!this.snackbarMessage;
        }

        private closeSnackbar() {
            this.$store.commit('snack', '');
        }
    }
</script>