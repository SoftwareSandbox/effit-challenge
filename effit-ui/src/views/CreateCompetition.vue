<template>
    <v-layout align-start justify-start column fill-height>
        <h2>New Competition</h2>
        <v-form>
            <v-text-field
                    v-model="competition.name"
                    :counter="50"
                    label="Name"
                    required
            ></v-text-field>

            <date-field label="Starts" v-model="competition.startDate"></date-field>
            <date-field label="Ends" v-model="competition.endDate"></date-field>

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
    import DateField from '@/components/DateField.vue';

    @Component({
        components: {DateField},
    })
    export default class CreateCompetition extends Vue {
        protected competition = {
            name: '',
            startDate: new Date().toISOString().substr(0, 10),
            endDate: new Date().toISOString().substr(0, 10),
        };
        protected successfullyCreatedCompetitionId: string = '';

        // snackbar stuff
        protected showSnackbar = false;
        protected snackbarMessage = '';
        protected snackbarTimeout = 3000;

        private submit() {
            this.$axios.post(`/api/competition`, this.competition)
                .then((res) => {
                    this.successfullyCreatedCompetitionId = res.headers.location;
                    this.snackbarMessage = `Successfully created your new Competition!`;
                    this.showSnackbar = true;
                })
                .then(() => this.resetForm());
        }

        private resetForm() {
            this.competition = {
                name: '',
                startDate: new Date().toISOString().substr(0, 10),
                endDate: new Date().toISOString().substr(0, 10),
            };
        }

        private closeSnackbar() {
            this.showSnackbar = false;
            this.snackbarMessage = '';
        }
    }
</script>
