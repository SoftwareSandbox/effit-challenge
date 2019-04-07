<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-layout align-start justify-start>
        <v-data-table
                :headers="headers"
                :items="challenges"
                class="elevation-1"
                align top
        >
            <template v-slot:items="props">
                <td class="text-xs-left">{{ props.item.name }}</td>
                <td class="text-xs-right">{{ props.item.points }}</td>
                <td class="text-xs-left">{{ props.item.description }}</td>
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
    export default class ChallengesOverview extends Vue {

        public headers = [
            {text: 'Name', value: 'name'},
            {text: 'Points', value: 'points'},
            {text: 'Description', value: 'description'},
        ];
        public challenges: any[] = [];

        public mounted() {
            this.$axios
                .get('/api/challenge')
                .then(({data}) => this.challenges = data);
        }
    }
</script>
