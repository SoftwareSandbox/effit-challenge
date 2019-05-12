<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-data-table
            :headers="headers"
            :items="challenges"
            class="elevation-1"
            align top
    >
        <template v-slot:items="props">
            <tr @click="rowHandler(props.item)">
                <td class="text-xs-left">{{ props.item.name }}</td>
                <td class="text-xs-right">{{ props.item.points }}</td>
                <td class="text-xs-left">{{ props.item.description }}</td>
            </tr>
        </template>
    </v-data-table>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';
    import {Challenge} from '@/model/Challenge';

    @Component({
        components: {},
    })
    export default class ChallengesTable extends Vue {
        private headers = [
            {text: 'Name', value: 'name'},
            {text: 'Points', value: 'points'},
            {text: 'Description', value: 'description'},
        ];

        @Prop({type: Array}) private challenges !: Challenge[];
        @Prop({type: Function}) private rowHandler !: (challenge: Challenge) => void;
    }
</script>
