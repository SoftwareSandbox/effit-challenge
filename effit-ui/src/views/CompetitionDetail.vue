<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-layout align-start justify-start column fill-height>
        <h3>
            <v-layout row justify-space-between align-center>
                <div>{{competition.startDate}} - {{competition.endDate}}</div>
                <v-btn @click="hostAgain">Host again</v-btn>
            </v-layout>
        </h3>

        <v-btn v-if="!competition.started" @click="startCompetition">Start Competition</v-btn>

        <div v-if="!competition.started">
            <v-item-group>
                Current competitors:
                <v-chip v-for="competitor in competitors"
                        close
                        :key="competitor.id"
                        @input="removeCompetitor(competitor)">
                    {{competitor.name}}
                </v-chip>
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
        </div>

        <editable-challenges-table
                v-if="!competition.started"
                :challenges="competition.challenges"
                :competitionId="competitionId"
        ></editable-challenges-table>

        <v-btn v-if="competition.started" @click="navigateToCompleteChallenges">Complete Challenges</v-btn>

        <div v-if="competition.started">
            <h3>Scoreboard</h3>
            <v-data-table
                    :headers="scoreTableHeaders"
                    :custom-sort="byTotalScore"
                    :items="competitors"
                    :rows-per-page-items="rowsPerPageItems"
                    class="elevation-1"
            >
                <template v-slot:items="props">
                    <tr>
                        <td class="text-xs-right">{{ props.index + 1 }}</td>
                        <td class="text-xs-left">{{ props.item.name }}</td>
                        <td class="text-xs-right">{{ props.item.totalScore }}</td>
                    </tr>
                </template>
            </v-data-table>
        </div>
    </v-layout>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';
    import {Challenge} from '@/model/Challenge';
    import {Competition, Competitor} from '@/model/Competition';
    import {Route} from 'vue-router';
    import EditableChallengesTable from '@/components/EditableChallengesTable.vue';

    function beforeRouteEnterNavGuard(to: Route, from: Route, next: any) {
        return next((vm: CompetitionDetail) => {
            return vm.$axios.get(`/api/competition/${vm.competitionId}`)
                .then(() => to)
                .catch(() => vm.$router.push('/404'));
        });
    }

    @Component({
        components: {EditableChallengesTable},
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
            started: false,
        };

        protected scoreTableHeaders = [
            {text: '#', sortable: false, width: '4%'},
            {text: 'Name', value: 'name', sortable: false},
            {text: 'Points', value: 'points', sortable: false},
        ];
        protected rowsPerPageItems = [10, 25, {text: '$vuetify.dataIterator.rowsPerPageAll', value: -1}];

        public async refreshCompetition() {
            this.competition = (await this.$axios.get(`/api/competition/${this.competitionId}`)).data;
        }

        public async startCompetition() {
            await this.$axios.post(`/api/competition/${this.competitionId}/start`);
            await this.refreshCompetition();
            this.$store.commit('showSnackMessage', {
                message: 'Competition started!',
                undo: () => this.unstartCompetition(),
            });
        }

        public async unstartCompetition() {
            await this.$axios.post(`/api/competition/${this.competitionId}/unstart`);
            await this.refreshCompetition();
        }

        private async mounted() {
            await this.refreshCompetition();
            this.$store.commit('updateTitle', `${this.competition.name}`);
        }

        private get competitors() {
            return this.competition.competitors;
        }

        private navigateToCompleteChallenges(challenge: Challenge) {
            this.$router.push(`/competitions/${this.competitionId}/complete`);
        }

        private async addCompetitor() {
            await this.$axios.post(`/api/competition/${this.competitionId}/addCompetitor`, {name: this.competitorName});
            this.competitorName = '';
            await this.refreshCompetition();
        }

        private async removeCompetitor(competitorToRemove: Competitor) {
            const foundCompetitorToRemove = this.competition.competitors.find((comp) => comp.id === competitorToRemove.id);
            await this.$axios.post(`/api/competition/${this.competitionId}/removeCompetitor`, foundCompetitorToRemove);
            await this.refreshCompetition();
        }

        private byTotalScore(items: Competitor[], index: number): Competitor[] {
            return items.sort((a, b) => b.totalScore - a.totalScore);
        }

        private hostAgain() {
            // navigate to createCompetition with an ID of this.competition that should serve as a clone
            this.$router.push(`/competitions/hostagain/${this.competitionId}`);
        }
    }
</script>
