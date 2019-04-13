<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-layout align-start justify-start>
        <v-data-table
                :headers="headers"
                :items="competitions"
                class="elevation-1"
                align top
        >
            <template v-slot:items="props">
                <router-link tag="td"
                             class="text-xs-left competition-name"
                             :to="`/competitions/${props.item.competitionId.id}`">{{ props.item.name }}</router-link>
                <td class="text-xs-right">{{ props.item.startDate }}</td>
                <td class="text-xs-left">{{ props.item.endDate }}</td>
            </template>
        </v-data-table>
    </v-layout>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';

    @Component({
        props: {source: String},
        components: {},
    })
    export default class CompetitionsOverview extends Vue {

        public headers = [
            {text: 'Name', value: 'name'},
            {text: 'Starts', value: 'startDate'},
            {text: 'Ends', value: 'endDate'},
        ];
        public competitions: any[] = [];

        public mounted() {
            this.$axios
                .get('/api/competition')
                .then(({data}) => this.competitions = data);
        }

    }
</script>

<style>
    .competition-name:hover {
        cursor: pointer;
    }
</style>