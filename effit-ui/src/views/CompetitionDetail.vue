<template>
    <v-layout align-start justify-start column fill-height>
        <h2>{{competition.startDate}} - {{competition.endDate}}</h2>
        <challenges-table
                :challenges="competition.challenges"
                :rowHandler="navigateToMarkAsCompleted">
        </challenges-table>
        <v-item-group>
            Current competitors:
            <v-item v-for="competitor in competitors" :key="competitor.id" :disabled="true">
                <em>{{competitor.name}}, </em>
            </v-item>
        </v-item-group>
        <v-form @submit.prevent="addCompetitor">
            <v-text-field
                    v-model="competitorName"
                    :counter="50"
                    label="Competitor to add"
                    required
            ></v-text-field>

            <v-btn type="submit" :disabled="!this.competitorName">add competitor</v-btn>
        </v-form>
    </v-layout>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';
    import ChallengesTable from '@/components/ChallengesTable.vue';
    import {Challenge} from '@/model/Challenge';
    import {noop} from 'vue-class-component/lib/util';
    import {Competition} from '@/model/Competition';
    import {Route} from 'vue-router';

    function beforeRouteEnterNavGuard(to: Route, from: Route, next: any) {
        return next((vm: CompetitionDetail) => {
            return vm.$axios.get(`/api/competition/${vm.competitionId}`)
                .then(() => to)
                .catch(() => vm.$router.push('/404'));
        });
    }

    @Component({
        components: {ChallengesTable},
        beforeRouteEnter: beforeRouteEnterNavGuard,
    })
    export default class CompetitionDetail extends Vue {
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
            this.$store.commit('updateTitle', `${this.competition.name}`);
        }

        private navigateToMarkAsCompleted(challenge: Challenge) {
            this.$router.push(`/competitions/${this.competitionId}/complete/${challenge.id}`);
        }

        private async addCompetitor() {
            await this.$axios.post(`/api/competition/${this.competitionId}/addCompetitor`, {name: this.competitorName})
                .catch(noop);
            this.competitorName = '';
            await this.refreshCompetition();
        }

        private get competitors() {
            return this.competition.competitors;
        }
    }
</script>
