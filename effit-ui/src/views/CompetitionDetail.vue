<template>
    <v-layout align-start justify-start>
        <h1>{{competition.name}} : {{competition.startDate}} - {{competition.endDate}}</h1>
    </v-layout>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';

    @Component({
        components: {},
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
