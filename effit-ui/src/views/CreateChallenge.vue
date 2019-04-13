<template>
    <v-layout align-start justify-start column fill-height>
        <h2>Creating a new Challenge are we?</h2>
        <v-form>
            <v-text-field
                    v-model="challenge.name"
                    :counter="50"
                    label="Name"
                    required
            ></v-text-field>
            <v-text-field
                    v-model="challenge.points"
                    label="Points"
                    required
                    type="number"
            ></v-text-field>
            <v-textarea
                    v-model="challenge.description"
                    auto-grow
                    box
                    label="Description"
                    rows="1"
            ></v-textarea>
            <v-btn @click="submit">submit</v-btn>
        </v-form>
    </v-layout>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import ChallengesTable from '@/components/ChallengesTable.vue';

    @Component({
        components: {ChallengesTable},
    })
    export default class CreateChallenge extends Vue {
        protected challenge = {name: '', points: 0, description: ''};

        private submit() {

        }

        // private mounted() {
        //     this.$axios.get(`/api/challenge`)
        //         .then(({data}) => this.competition = data);
        // }
    }
</script>
