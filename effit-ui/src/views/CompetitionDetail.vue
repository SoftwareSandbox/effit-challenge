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

    @Component({
        components: {ChallengesTable},
    })
    export default class CompetitionDetail extends Vue {
        @Prop({type: String}) protected competitionId!: string;
        protected competition = {name: '', startDate: '', endDate: ''};

        private mounted() {
            this.$axios.get(`/api/competition/${this.competitionId}`)
                .then(({data}) => this.competition = data)
                .then(() => this.$store.commit('routerViewWasSwitched', `${this.competition.name}`));
        }

        private navigateToMarkAsCompleted(challenge: Challenge) {
            this.$router.push(`/competitions/${this.competitionId}/complete/${challenge.id}`);
        }
    }
</script>
