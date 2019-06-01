<template>
    <v-layout align-start justify-start column fill-height>
        <h1>Yikes!</h1>
        <p>Something clearly went wrong.</p>
        <p>If you got this url from a friend, ask them to resend the link to you.</p>
    </v-layout>
</template>

<script lang="ts">
    import {Component, Vue} from 'vue-property-decorator';

    @Component({
        components: {},
    })
    export default class FourOhFour extends Vue {
        public mounted() {
            this.$store.commit('updateTitle', `Page not found`);
        }
    }
</script>