<template>
    <v-layout align-start justify-start column fill-height>
        <p>{{challenge.description}}</p>

        <!-- input text field for Competitor that completed the challenge goes here -->
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

        private mounted() {
            this.$axios.get(`/api/challenge/${this.challengeId}`)
                .then(({data}) => this.challenge = data)
                .then(() => this.$store.commit('updateTitle', `Who completed ${this.challenge.name}?`))
                .catch(() => {/* noop, is already handled by interceptor in Main*/});
        }
    }
</script>
