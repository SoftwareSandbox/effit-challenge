<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-menu
            v-model="datePickerMenu"
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
                    v-model="value"
                    @input="emitOnInput"
                    :label="label"
                    prepend-icon="event"
                    readonly
                    v-on="on"
            ></v-text-field>
        </template>
        <v-date-picker :value="value" @input="emitOnInput"></v-date-picker>
    </v-menu>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from 'vue-property-decorator';

    @Component({
        components: {},
    })
    export default class DateField extends Vue {
        @Prop({type: String}) public label !: string;
        @Prop({type: String}) public value !: string;
        protected datePickerMenu = false;

        public emitOnInput(value: string) {
            this.datePickerMenu = false;
            this.$emit('input', value);
        }
    }
</script>