<template>
    <v-layout align-start justify-start column fill-height>
        <h2>Challenges</h2>
        <challenges-table :challenges="challenges"></challenges-table>
    </v-layout>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import ChallengesTable from '@/components/ChallengesTable.vue';

    @Component({
        components: {ChallengesTable},
    })
    export default class ChallengesOverview extends Vue {
        private challenges: any[] = [];

        public mounted() {
            this.$axios
                .get('/api/challenge')
                .then(({data}) => this.challenges = data);
        }
    }
</script>