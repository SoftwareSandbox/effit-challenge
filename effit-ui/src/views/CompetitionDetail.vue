<template>
    <v-layout align-start justify-start>
        <v-container>
            <v-layout align-start justify-start>
                <h1>{{competition.name}} : {{competition.startDate}} - {{competition.endDate}}</h1>
            </v-layout>
            <v-layout align-start justify-start>
                <challenges-table :challenges="competition.challenges"></challenges-table>
            </v-layout>
        </v-container>
    </v-layout>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';
    import ChallengesTable from '@/components/ChallengesTable.vue';

    @Component({
        components: {ChallengesTable},
    })
    export default class CompetitionDetail extends Vue {
        @Prop({type: String}) protected name!: string;
        protected competition = {name: '', startDate: '', endDate: ''};

        private mounted() {
            this.$axios.get(`/api/competition/${this.name}`)
                .then(({data}) => this.competition = data);
        }
    }
</script>
