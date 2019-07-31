<template>
    <v-layout column align-start justify-start fill-height>
        <h2>{{competition.startDate}} - {{competition.endDate}}</h2>
        <challenges-table
                :challenges="competition.challenges"
                :rowHandler="navigateToMarkAsCompleted">
        </challenges-table>

        <router-link :to="`/competitions/${competitionId}`" tag="v-btn">
            cancel
        </router-link>
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
            started: false,
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
