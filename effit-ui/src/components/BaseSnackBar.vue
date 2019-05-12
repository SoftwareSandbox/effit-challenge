<template>
    <v-snackbar
            v-model="showSnackbar"
            top
            :color="color"
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

        get color() {
            return this.$store.state.snackColor;
        }

        get snackbarMessage() {
            return this.$store.state.snack;
        }

        get showSnackbar() {
            return !!this.snackbarMessage;
        }

        private closeSnackbar() {
            this.$store.commit('snack', {message: ''});
        }
    }
</script>