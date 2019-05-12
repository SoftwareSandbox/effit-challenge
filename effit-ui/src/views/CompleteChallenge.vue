<template>
    <v-layout align-start justify-start column fill-height>
        <p>{{challenge.description}}</p>
        <p>Worth {{challenge.points}} point(s)</p>

        <v-form>
            <v-text-field
                    v-model="competitorName"
                    :counter="50"
                    label="Competitor Name"
                    required
            ></v-text-field>

            <v-btn @click="submit">submit</v-btn>
        </v-form>
    </v-layout>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';
    import ChallengesTable from '@/components/ChallengesTable.vue';
    import {Challenge} from '@/model/Challenge';

    @Component({
        components: {},
    })
    export default class CompleteChallenge extends Vue {
        @Prop({type: String}) protected competitionId!: string;
        @Prop({type: String}) protected challengeId!: string;
        protected challenge: Challenge = {
            id: '',
            name: '',
            points: 0,
            description: '',
        };
        protected competitorName: string = '';

        private mounted() {
            // TODO: refactor this to also include the competition id,
            // TODO: to make it explicit that its not just any challenge,
            // TODO: but specifically the one belonging to this competitionId.
            this.$axios.get(`/api/challenge/${this.challengeId}`)
                .then(({data}) => this.challenge = data)
                .then(() => this.$store.commit('updateTitle', `Who completed ${this.challenge.name}?`))
                .catch(() => {/* noop, is already handled by interceptor in Main*/});
        }

        private submit() {
            // this.$axios.post(`/api/competition/${this.competitionId}/complete/${this.challengeId}`, {competitorName: this.competitorName})
            //     .then(() => this.$router.push(`/competitions/${this.competitionId}`))
            //     .then(() => this.$store.commit('showSnackMessage', {message: `Congrats to ${this.competitorName} on completing a challenge!`}))
            //     .catch(() => {/* noop, is already handled by interceptor in Main*/});
        }
    }
</script>
