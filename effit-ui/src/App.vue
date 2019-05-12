<template>
    <v-app id="inspire" dark>
        <v-navigation-drawer
                v-model="drawer"
                clipped
                fixed
                app
        >
            <v-list dense>
                <router-link to="/challenges" tag="v-list-tile">
                    <v-list-tile-action>
                        <v-icon>stars</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>Challenges</v-list-tile-title>
                    </v-list-tile-content>
                </router-link>

                <router-link to="/challenges/create" tag="v-list-tile">
                    <v-list-tile-action>
                        <v-icon>stars</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>Create a new Challenge</v-list-tile-title>
                    </v-list-tile-content>
                </router-link>

                <router-link to="/competitions" tag="v-list-tile">
                    <v-list-tile-action>
                        <v-icon>stars</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>Competitions</v-list-tile-title>
                    </v-list-tile-content>
                </router-link>

                <router-link to="/competitions/create" tag="v-list-tile">
                    <v-list-tile-action>
                        <v-icon>stars</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>Create a new Competition</v-list-tile-title>
                    </v-list-tile-content>
                </router-link>

                <router-link to="/about" tag="v-list-tile">
                    <v-list-tile-action>
                        <v-icon>settings</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>About</v-list-tile-title>
                    </v-list-tile-content>
                </router-link>

            </v-list>
        </v-navigation-drawer>
        <v-toolbar app fixed clipped-left>
            <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
            <v-toolbar-title>{{title}}</v-toolbar-title>
        </v-toolbar>
        <v-content>
            <v-container fluid fill-height>

                <router-view></router-view>

            </v-container>
        </v-content>
        <v-footer app fixed>
            <span>&copy; 2019</span>
        </v-footer>

        <v-snackbar
                v-model="showErrorSnackbar"
                top
                color="red darken-2"
                :timeout="6000"
                :vertical="true"
                :multi-line="true"
        >
            {{ errorSnackbarMessage }}
            <v-btn dark flat @click="closeErrorSnackbar()">Close</v-btn>
        </v-snackbar>

        <base-snack-bar></base-snack-bar>
    </v-app>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';
    import BaseSnackBar from '@/components/BaseSnackBar.vue';

    @Component({components: {BaseSnackBar}})
    export default class App extends Vue {
        protected title = 'EFFIT';
        protected drawer = null;

        // ErrorSnackbar stuff
        protected showErrorSnackbar = false;
        protected errorSnackbarMessage = '';

        private mounted() {
            this.$axios.interceptors.response.use(
                (response) => response,
                (error) => {
                    this.errorSnackbarMessage = error.response.data.message;
                    this.showErrorSnackbar = true;
                });
        }

        private closeErrorSnackbar() {
            this.showErrorSnackbar = false;
            this.errorSnackbarMessage = '';
        }
    }
</script>
