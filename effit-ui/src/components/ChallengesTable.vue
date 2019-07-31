<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">

    <div>
        <v-toolbar flat>
            <v-layout row align-center justify-space-between>
                <v-toolbar-title>Challenges</v-toolbar-title>
                <v-btn v-if="isEditable" dark @click="showDialog = true">Add Challenge</v-btn>
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
                        <v-btn colors="primary" flat @click="closeDialog">Cancel</v-btn>
                        <v-btn colors="primary" flat @click="saveEdit">Save</v-btn>
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
                    <td class="justify-center layout px-0" v-if="isEditable">
                        <v-icon
                                small
                                class="mr-2"
                                @click="editItem(props.item)"
                        >
                            edit
                        </v-icon>
                        <v-icon
                                small
                                @click="deleteItem(props.item)"
                        >
                            delete
                        </v-icon>
                    </td>
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
    export default class ChallengesTable extends Vue {
        private headers: Array<{ text: string, value: string, sortable?: boolean }> = [
            {text: 'Name', value: 'name'},
            {text: 'Points', value: 'points'},
            {text: 'Description', value: 'description'},
            {text: 'Actions', value: 'name', sortable: false },
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
        @Prop({type: Boolean}) private isEditable: boolean = false;

        private mounted() {
            if (this.isEditable) {
                this.headers.push({ text: 'Actions', value: 'name', sortable: false });
            }
        }

        get formTitle(): string {
            return this.editedIndex < 0 ? 'New Challenge' : 'Edit Challenge';
        }

        private handleRow(challenge: Challenge) {
            if (this.rowHandler !== undefined) {
                return this.rowHandler(challenge);
            }
        }

        private editItem(challenge: Challenge) {
            this.editedIndex = this.challenges.indexOf(challenge);
            this.editableChallenge = Object.assign({}, challenge);
            this.showDialog = true;
        }

        private deleteItem(challenge: Challenge) {
            const index = this.challenges.indexOf(challenge);
            confirm('Are you sure you want to delete this challenge?') && this.challenges.splice(index, 1);
        }

        private saveEdit() {
            if (this.editedIndex > -1) {
                Object.assign(this.challenges[this.editedIndex], this.editableChallenge);
            } else {
                this.challenges.push(Object.assign({}, this.editableChallenge));
            }
            this.closeDialog();
        }

        private closeDialog() {
            this.showDialog = false;
            setTimeout(() => {
                this.editableChallenge = Object.assign({}, this.defaultChallenge);
                this.editedIndex = -1;
            }, 300);
        }
    }
</script>
