<template>
    <v-layout align-start justify-start column fill-height>
        <v-form>
            <v-text-field
                    v-model="challenge.name"
                    :counter="50"
                    label="Name"
                    required
            ></v-text-field>
            <v-text-field
                    v-model="challenge.points"
                    label="Points"
                    required
                    type="number"
            ></v-text-field>
            <v-textarea
                    v-model="challenge.description"
                    auto-grow
                    box
                    label="Description"
                    rows="1"
            ></v-textarea>
            <v-btn @click="submit">submit</v-btn>
        </v-form>

        <v-snackbar
                v-model="showSnackbar"
                top
                color="cyan darken-2"
                :timeout="snackbarTimeout"
        >
            {{ snackbarMessage }}
            <v-btn dark flat @click="closeSnackbar()">Close</v-btn>
        </v-snackbar>
    </v-layout>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';

    @Component({
        components: {},
    })
    export default class CreateChallenge extends Vue {
        protected challenge = {name: '', points: 0, description: ''};
        protected successfullyCreatedChallengeId: string = '';
        // snackbar stuff
        protected showSnackbar = false;
        protected snackbarMessage = '';
        protected snackbarTimeout = 3000;

        private mounted() {
            this.$store.commit('routerViewWasSwitched', 'New Challenge');
        }

        private submit() {
            this.$axios.post(`/api/challenge`, this.challenge)
                .then((res) => {
                    this.successfullyCreatedChallengeId = res.headers.location;
                    this.snackbarMessage = `Successfully created your new Challenge!`;
                    this.showSnackbar = true;
                })
                .then(() => this.resetForm())
                .catch(() => {/* noop, is already handled by interceptor in Main*/});
        }

        private resetForm() {
            this.challenge = {name: '', points: 0, description: ''};
        }

        private closeSnackbar() {
            this.showSnackbar = false;
            this.snackbarMessage = '';
        }
    }
</script>
