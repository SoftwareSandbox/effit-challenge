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
        </router-link >

        <router-link to="/challenges/create" tag="v-list-tile">
          <v-list-tile-action>
            <v-icon>stars</v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title>Create a new Challenge</v-list-tile-title>
          </v-list-tile-content>
        </router-link >

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
        </router-link >

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
  </v-app>
</template>

<script>
  import {Component, Vue} from 'vue-property-decorator';

  @Component({
  })
  export default class App extends Vue {
      title = 'EFFIT';
      drawer = null;
  }
</script>
