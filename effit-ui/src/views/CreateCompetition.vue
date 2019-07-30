<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-layout align-start justify-start column fill-height>
        <v-form>
            <v-text-field
                    v-model="name"
                    :counter="50"
                    label="Name"
                    :rules="nameValidators"
            ></v-text-field>

            <date-field label="Starts" v-model="startDate"></date-field>
            <date-field label="Ends" v-model="endDate"></date-field>

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

    </v-layout>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import DateField from '@/components/DateField.vue';
    import ChallengesTable from '@/components/ChallengesTable.vue';
    import ChallengeCard from '@/components/ChallengeCard.vue';
    import {Challenge} from '@/model/Challenge';

    type SelectableChallenge = Challenge & { selected: boolean };

    @Component({
        components: {ChallengeCard, ChallengesTable, DateField},
    })
    export default class CreateCompetition extends Vue {
        protected name = '';
        protected backedStartDate = new Date().toISOString().substr(0, 10);
        protected endDate = new Date().toISOString().substr(0, 10);

        protected successfullyCreatedCompetitionId: string = '';
        protected challenges: SelectableChallenge[] = [];

        // data iterator stuff
        protected rowsPerPageItems = [4, 8, 12];
        protected pagination = {rowsPerPage: 4};

        private async mounted() {
            this.$store.commit('updateTitle', 'New Competition');
            this.challenges = (await this.$axios.get(`/api/challenge`)).data.map(this.expandWithSelectedProperty);
            this.rowsPerPageItems.push(this.challenges.length);
        }

        private expandWithSelectedProperty(challenge: Challenge) {
            return {...challenge, ...{selected: false}};
        }

        private async submit() {
            this.successfullyCreatedCompetitionId = (await this.$axios.post(`/api/competition`, this.competition))
                .headers.location;
            await this.$axios.post(`/api/competition/${this.successfullyCreatedCompetitionId}/addChallenges`,
                this.selectedChallenges());
            this.showSnackBar(`Successfully created your new Competition!`);
            this.navigateToCreatedCompetition();
        }

        set startDate(newDate: string) {
            this.backedStartDate = new Date(newDate).toISOString().substr(0, 10);
            this.endDate = new Date(newDate).toISOString().substr(0, 10);
        }

        get startDate() {
            return this.backedStartDate;
        }

        get competition() {
            return {
                name: this.name,
                startDate: this.startDate,
                endDate: this.endDate,
            };
        }

        get nameValidators(): [((value: any) => boolean | string)] {
            return [(value) => !!value || 'Required.'];
        }

        private selectedChallenges() {
            return this.challenges.filter((c) => c.selected);
        }

        private navigateToCreatedCompetition() {
            this.$router.push(`/competitions/${this.successfullyCreatedCompetitionId}`);
        }

        private showSnackBar(message: string) {
            this.$store.commit('showSnackMessage', {message});
        }


    }
</script>
