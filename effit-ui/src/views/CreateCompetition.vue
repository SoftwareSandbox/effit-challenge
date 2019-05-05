<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
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

            <v-data-iterator
                    :items="challenges"
                    :rows-per-page-items="rowsPerPageItems"
                    :pagination.sync="pagination"
                    content-tag="div"
                    row
            >
                <template v-slot:header><h3>Select your challenges for this competition</h3></template>
                <template v-slot:item="props">
                    <v-layout column justify-start>
                        <challenge-card :challenge="props.item"></challenge-card>
                        <v-divider inset></v-divider>
                    </v-layout>
                </template>
            </v-data-iterator>

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
    import ChallengesTable from '@/components/ChallengesTable.vue';
    import ChallengeCard from '@/components/ChallengeCard.vue';

    type Challenge = {name: string, points: number, description: string};
    type SelectableChallenge = Challenge & {selected: boolean};

    @Component({
        components: {ChallengeCard, ChallengesTable, DateField},
    })
    export default class CreateCompetition extends Vue {
        protected competition = {
            name: '',
            startDate: new Date().toISOString().substr(0, 10),
            endDate: new Date().toISOString().substr(0, 10),
        };
        protected successfullyCreatedCompetitionId: string = '';
        protected challenges: Array<SelectableChallenge> = [];

        // data iterator stuff
        protected rowsPerPageItems = [4, 8, 12];
        protected pagination = { rowsPerPage: 4 };

        // snackbar stuff
        protected showSnackbar = false;
        protected snackbarMessage = '';
        protected snackbarTimeout = 3000;

        private mounted() {
            this.$axios.get(`/api/challenge`)
                .then(({data}: {data: Array<Challenge>}) => this.challenges = data.map(this.expandWithSelectedProperty));
        }

        private expandWithSelectedProperty(challenge: Challenge) {
            return {...challenge, ...{selected: false}};
        }

        private submit() {
            this.$axios.post(`/api/competition`, this.competition)
                .then((res) => this.successfullyCreatedCompetitionId = res.headers.location)
                .then(() => this.$axios.post(`/api/competition/${this.successfullyCreatedCompetitionId}/addChallenges`, this.selectedChallenges()))
                .then(() => {
                    this.snackbarMessage = `Successfully created your new Competition!`;
                    this.showSnackbar = true;
                })
                .then(() => this.resetForm());
        }

        private selectedChallenges() {
            return this.challenges.filter(c => c.selected);
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
