<template>
    <v-layout align-start justify-start column fill-height>
        <h2>{{competition.name}} : {{competition.startDate}} - {{competition.endDate}}</h2>
        <challenges-table :challenges="competition.challenges"></challenges-table>
    </v-layout>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';
    import ChallengesTable from '@/components/ChallengesTable.vue';

    @Component({
        components: {ChallengesTable},
    })
    export default class CompetitionDetail extends Vue {
        @Prop({type: String}) protected competitionId!: string;
        protected competition = {name: '', startDate: '', endDate: ''};

        private mounted() {
            this.$axios.get(`/api/competition/${this.competitionId}`)
                .then(({data}) => this.competition = data);
        }
    }
</script>
