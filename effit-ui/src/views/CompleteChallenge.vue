<template>
    <v-layout align-start justify-start column fill-height>
        <p>{{challenge.description}}</p>
        <p>Worth {{challenge.points}} point(s)</p>

        <v-form>
            <v-select
                    v-model="selectedCompetitor"
                    :items="competitors"
                    item-text="name"
                    return-object
                    label="Competitor"
            ></v-select>

            <v-btn @click="submit">submit</v-btn>
        </v-form>
    </v-layout>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';
    import {Challenge} from '@/model/Challenge';
    import {Competitor} from '@/model/Competition';
    import {Route} from 'vue-router';

    function beforeRouteEnterNavGuard(to: Route, from: Route, next: any) {
        return next((vm: CompleteChallenge) => {
            return vm.$axios.get(`/api/competition/${vm.competitionId}`)
                .then(() => to)
                .catch(() => vm.$router.push('/404'));
        });
    }

    @Component({
        components: {},
        beforeRouteEnter: beforeRouteEnterNavGuard,
    })
    export default class CompleteChallenge extends Vue {
        @Prop({type: String}) public competitionId!: string;
        @Prop({type: String}) protected challengeId!: string;
        protected challenge: Challenge = {
            id: '',
            name: '',
            points: 0,
            description: '',
        };
        protected selectedCompetitor: Competitor = {id: '', name: ''};
        private competitors: Competitor[] = [];

        private async mounted() {
            // TODO: refactor this to also include the competition id,
            // TODO: to make it explicit that its not just any challenge,
            // TODO: but specifically the one belonging to this competitionId.
            this.challenge = (await this.$axios.get(`/api/challenge/${this.challengeId}`)).data;
            this.$store.commit('updateTitle', `Who completed ${this.challenge.name}?`);
            await this.fetchCompetitors();
        }

        private submit() {
            const competitor = {competitorId: this.selectedCompetitor.id};
            this.$axios.post(`/api/competition/${this.competitionId}/complete/${this.challengeId}`, competitor)
                .then(() => this.showSnackBar(`Congrats to ${this.selectedCompetitor.name} on completing a challenge!`))
                .then(() => this.navigateToCompetition())
                .catch(() => {/* noop, is already handled by interceptor in App.vue*/});
        }

        private showSnackBar(message: string) {
            this.$store.commit('showSnackMessage', {message});
        }

        private navigateToCompetition() {
            this.$router.push(`/competitions/${this.competitionId}`);
        }

        private async fetchCompetitors() {
            this.competitors = (await this.$axios.get(`/api/competition/${this.competitionId}`)).data.competitors;
        }
    }
</script>
