<template>
  <v-layout column align-start justify-start fill-height>
    <p>It helps you organise competitions among friends that feature challenges. More specifically it facilitates administration.</p>
    <p>Get started now! First <router-link to="challenges/create">create some challenges</router-link>, to then <router-link to="competitions/create">create a competition</router-link>, and inviting your friends!</p>
    <p>Remember to play nice!</p>
  </v-layout>
</template>

<script lang="ts">

  import {Component, Vue} from 'vue-property-decorator';

  @Component({
    components: {},
  })
  export default class About extends Vue {
    public mounted() {
      this.$store.commit('updateTitle', 'Effit Challenge');
    }
  }
</script>
