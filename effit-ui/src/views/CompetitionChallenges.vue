<template>
    <v-layout align-start justify-start column fill-height>
        <h2>{{competition.startDate}} - {{competition.endDate}}</h2>
        <challenges-table
                :challenges="competition.challenges"
                :rowHandler="navigateToMarkAsCompleted">
        </challenges-table>
    </v-layout>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';
    import ChallengesTable from '@/components/ChallengesTable.vue';
    import {Challenge} from '@/model/Challenge';
    import {Competition} from '@/model/Competition';
    import {Route} from 'vue-router';

    function beforeRouteEnterNavGuard(to: Route, from: Route, next: any) {
        return next((vm: CompetitionChallenges) => {
            return vm.$axios.get(`/api/competition/${vm.competitionId}`)
                .then(() => to)
                .catch(() => vm.$router.push('/404'));
        });
    }

    @Component({
        components: {ChallengesTable},
        beforeRouteEnter: beforeRouteEnterNavGuard,
    })
    export default class CompetitionChallenges extends Vue {
        @Prop({type: String}) public competitionId!: string;
        protected competitorName: string = '';
        protected competition: Competition = {
            name: '',
            startDate: '',
            endDate: '',
            competitors: [],
        };

        public async refreshCompetition() {
            this.competition = (await this.$axios.get(`/api/competition/${this.competitionId}`)).data;
        }

        private async mounted() {
            await this.refreshCompetition();
            this.$store.commit('updateTitle', `Complete Challenges in ${this.competition.name}`);
        }

        private navigateToMarkAsCompleted(challenge: Challenge) {
            this.$router.push(`/competitions/${this.competitionId}/complete/${challenge.id}`);
        }
    }
</script>
