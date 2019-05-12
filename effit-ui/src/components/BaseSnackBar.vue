<template>
    <v-snackbar
            v-model="showSnackbar"
            top
            :color="color"
            :timeout="6000"
            :vertical="true"
            :multi-line="true"
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
            return this.$store.state.snackMessage;
        }

        get showSnackbar() {
            return this.$store.state.snackShow;
        }

        set showSnackbar(bool: boolean) {
            this.$store.commit('showSnackbar', bool);
        }

        private closeSnackbar() {
            this.$store.commit('showSnackbar', false);
        }
    }
</script>