<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">

    <div>
        <v-toolbar flat>
            <v-layout row align-center justify-space-between>
                <v-toolbar-title>Challenges</v-toolbar-title>
                <v-btn dark @click="showDialog = true">Add Challenge</v-btn>
            </v-layout>
            <v-dialog v-model="showDialog" max-width="500px">
                <v-card>
                    <v-card-title>
                        <span class="headline">{{ formTitle }}</span>
                    </v-card-title>

                    <v-card-text>
                        <v-container column align-start justify-start>
                            <v-text-field
                                    v-model="editableChallenge.name"
                                    :autofocus="true"
                                    :counter="50"
                                    label="Name"
                                    required
                            ></v-text-field>
                            <v-text-field
                                    v-model="editableChallenge.points"
                                    label="Points"
                                    required
                                    type="number"
                            ></v-text-field>
                            <v-textarea
                                    v-model="editableChallenge.description"
                                    auto-grow
                                    box
                                    label="Description"
                                    rows="1"
                            ></v-textarea>
                        </v-container>
                    </v-card-text>

                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn v-if="!editingNewChallenge" color="error"
                               flat @click="deleteItem(editableChallenge)">Delete</v-btn>
                        <v-btn flat @click="closeDialog">Cancel</v-btn>
                        <v-btn flat @click="saveEdit(editableChallenge)">Save</v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>
        </v-toolbar>

        <v-data-table
                :headers="headers"
                :items="challenges"
                class="elevation-1"
                align top
        >
            <template v-slot:items="props">
                <tr @click="handleRow(props.item)">
                    <td class="text-xs-left">{{ props.item.name }}</td>
                    <td class="text-xs-right">{{ props.item.points }}</td>
                    <td class="text-xs-left">{{ props.item.description }}</td>
                </tr>
            </template>
        </v-data-table>
    </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';
    import {Challenge} from '@/model/Challenge';

    @Component({
        components: {},
    })
    export default class EditableChallengesTable extends Vue {
        private headers: Array<{ text: string, value: string, sortable?: boolean }> = [
            {text: 'Name', value: 'name'},
            {text: 'Points', value: 'points'},
            {text: 'Description', value: 'description'},
        ];

        private editableChallenge: Challenge = {
            name: '',
            points: 0,
            description: '',
        };
        private defaultChallenge: Challenge = {
            name: '',
            points: 0,
            description: '',
        };
        private showDialog: boolean = false;
        private editedIndex = -1;

        @Prop({type: Array}) private challenges !: Challenge[];
        @Prop({type: Function}) private rowHandler !: (challenge: Challenge) => void;
        @Prop({type: String}) private competitionId !: string;

        get formTitle(): string {
            return this.editedIndex < 0 ? 'New Challenge' : 'Edit Challenge';
        }

        private handleRow(challenge: Challenge) {
            this.overruleCustomRowHandlerWithEdit(challenge);
        }

        private overruleCustomRowHandlerWithEdit(challenge: Challenge) {
            return this.editItem(challenge);
        }

        private editItem(challenge: Challenge) {
            this.editedIndex = this.challenges.indexOf(challenge);
            this.editableChallenge = Object.assign({}, challenge);
            this.showDialog = true;
        }

        private async deleteItem(challenge: Challenge) {
            const index = this.challenges.indexOf(challenge);
            confirm('Are you sure you want to delete this challenge?');
            await this.removeChallenge(challenge);
            await this.reloadChallenges();
            this.closeDialog();
        }

        private async saveEdit(challenge: Challenge) {
            if (this.editingNewChallenge) {
                await this.createAndAddNewChallenge(challenge);
                await this.reloadChallenges();
                this.showSnackBar('Created a new Challenge!');
            } else {
                await this.updateChallenge(challenge);
                await this.reloadChallenges();
                this.showSnackBar('Challenge successfully updated.');
            }
            this.closeDialog();
        }

        private get editingNewChallenge() {
            return this.editedIndex < 0;
        }

        private closeDialog() {
            this.showDialog = false;
            setTimeout(() => {
                this.editableChallenge = Object.assign({}, this.defaultChallenge);
                this.editedIndex = -1;
            }, 300);
        }

        private showSnackBar(message: string) {
            this.$store.commit('showSnackMessage', {message});
        }

        private async createAndAddNewChallenge(challenge: Challenge) {
            await this.$axios.post(`/api/competition/${this.competitionId}/addChallenges`, [challenge]);
        }

        private async updateChallenge(challenge: Challenge) {
            await this.$axios.put(`/api/challenge/${challenge.id}`, challenge);
        }

        private async removeChallenge(challenge: Challenge) {
            await this.$axios.post(`/api/competition/${this.competitionId}/removeChallenge/${challenge.id}`);
        }

        private async reloadChallenges() {
            this.challenges = (await this.$axios.get(`/api/competition/${this.competitionId}`)).data.challenges;
        }
    }
</script>
