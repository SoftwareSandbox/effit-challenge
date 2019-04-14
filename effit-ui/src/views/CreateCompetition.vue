<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-layout align-start justify-start column fill-height>
        <h2>New Competition</h2>
        <v-form>
            <v-text-field
                    v-model="competition.name"
                    :counter="50"
                    label="Name"
                    required
            ></v-text-field>
            <v-menu
                    v-model="startDateDatePickerMenu"
                    :close-on-content-click="false"
                    :nudge-right="40"
                    lazy
                    transition="scale-transition"
                    offset-y
                    full-width
                    min-width="290px"
            >
                <template v-slot:activator="{ on }">
                    <v-text-field
                            v-model="competition.startDate"
                            label="Starts"
                            prepend-icon="event"
                            readonly
                            v-on="on"
                    ></v-text-field>
                </template>
                <v-date-picker v-model="competition.startDate" @input="startDateDatePickerMenu = false"></v-date-picker>
            </v-menu>
            <v-menu
                    v-model="endDateDatePickerMenu"
                    :close-on-content-click="false"
                    :nudge-right="40"
                    lazy
                    transition="scale-transition"
                    offset-y
                    full-width
                    min-width="290px"
            >
                <template v-slot:activator="{ on }">
                    <v-text-field
                            v-model="competition.endDate"
                            label="Ends"
                            prepend-icon="event"
                            readonly
                            v-on="on"
                    ></v-text-field>
                </template>
                <v-date-picker v-model="competition.endDate" @input="endDateDatePickerMenu = false"></v-date-picker>
            </v-menu>
            <v-btn @click="submit">submit</v-btn>
        </v-form>

        <v-snackbar
                v-model="showSnackbar"
                top
                color="cyan darken-2"
                :timeout="snackbarTimeout"
        >
            {{ snackbarMessage }}
            <v-btn dark flat @click="closeSnackbar()">Close</v-btn>
        </v-snackbar>
    </v-layout>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';

    @Component({
        components: {},
    })
    export default class CreateCompetition extends Vue {
        protected competition = {name: '', startDate: new Date().toISOString().substr(0, 10), endDate: new Date().toISOString().substr(0, 10)};
        protected successfullyCreatedCompetitionId: string = '';
        // datepicker stuff
        protected startDateDatePickerMenu = false;
        protected endDateDatePickerMenu = false;

        // snackbar stuff
        protected showSnackbar = false;
        protected snackbarMessage = '';
        protected snackbarTimeout = 3000;

        private submit() {
            this.$axios.post(`/api/competition`, this.competition)
                .then((res) => {
                    this.successfullyCreatedCompetitionId = res.headers.location;
                    this.snackbarMessage = `Successfully created your new Competition!`;
                    this.showSnackbar = true;
                })
                .then(() => this.resetForm());
        }

        private resetForm() {
            this.competition = {name: '', startDate: new Date().toISOString().substr(0, 10), endDate: new Date().toISOString().substr(0, 10)};
        }

        private closeSnackbar() {
            this.showSnackbar = false;
            this.snackbarMessage = '';
        }
    }
</script>
