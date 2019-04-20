<template>
    <v-card>
        <v-layout row justify-space-between>
            <v-card-title><h3>{{challenge.name}}</h3></v-card-title>
            <div><v-chip title="Points">{{challenge.points}}</v-chip></div>
        </v-layout>
        <v-card-text>{{challenge.description}}</v-card-text>
    </v-card>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';

    @Component({
        components: {},
    })
    export default class ChallengeCard extends Vue {
        @Prop({type: Array}) public challenge !: { name: string, points: number, description: string };


    }
</script>
