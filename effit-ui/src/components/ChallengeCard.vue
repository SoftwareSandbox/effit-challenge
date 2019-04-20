<template>
    <v-card @click="emitOnClick">
        <v-layout row justify-space-between>
            <v-card-title><h3>{{challenge.name}}</h3></v-card-title>
            <div>
                <v-chip title="Points" :class="challenge.selected ? 'primary' : ''">{{challenge.points}}</v-chip>
            </div>
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
        @Prop({type: Object}) public challenge !: { name: string, points: number, description: string, selected: boolean };

        public emitOnClick() {
            this.challenge.selected = !this.challenge.selected;
            this.$emit('click', this.challenge);
        }

    }
</script>