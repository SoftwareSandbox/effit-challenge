<template>
    <v-layout align-start justify-start column fill-height>
        <h2>{{competition.startDate}} - {{competition.endDate}}</h2>
        <challenges-table
                :challenges="competition.challenges"
                :rowHandler="navigateToMarkAsCompleted">
        </challenges-table>
        <v-form @submit.prevent="addCompetitor">
            <v-text-field
                    v-model="competitorName"
                    :counter="50"
                    label="Competitor to add"
                    required
            ></v-text-field>

            <v-btn type="submit">add competitor</v-btn>
        </v-form>
    </v-layout>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';
    import ChallengesTable from '@/components/ChallengesTable.vue';
    import {Challenge} from '@/model/Challenge';
    import {noop} from 'vue-class-component/lib/util';

    @Component({
        components: {ChallengesTable},
    })
    export default class CompetitionDetail extends Vue {
        @Prop({type: String}) protected competitionId!: string;
        protected competition = {name: '', startDate: '', endDate: ''};
        protected competitorName : string = '';

        private mounted() {
            this.$axios.get(`/api/competition/${this.competitionId}`)
                .then(({data}) => this.competition = data)
                .then(() => this.$store.commit('updateTitle', `${this.competition.name}`));
        }

        private navigateToMarkAsCompleted(challenge: Challenge) {
            this.$router.push(`/competitions/${this.competitionId}/complete/${challenge.id}`);
        }

        private addCompetitor() {
            this.$axios.post(`/api/competition/${this.competitionId}/addCompetitor`, {name: this.competitorName})
                .catch(noop);
        }
    }
</script>
