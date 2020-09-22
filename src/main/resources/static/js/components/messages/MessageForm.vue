<template>
    <v-container>
        <v-row>
            <v-col><v-text-field label="Write your message!" outlined v-model="text" /></v-col>
            <v-col><v-btn @click="save">
                Save
            </v-btn></v-col>
        </v-row>
    </v-container>
</template>

<script>
    import { mapActions } from "vuex";
    export default {
        props: ['messageAttr'],
        data() {
            return {
                text: '',
                id: ''
            }
        },
        watch: {
            messageAttr(newVal, oldVal) {
                this.text = newVal.text
                this.id = newVal.id
            }
        },
        methods: {
            ...mapActions(['addMessageAction', 'updateMessageAction']),
            save() {
                const message = { 
                    text: this.text,
                    id: this.id 
                }
                if (this.id) {
                    this.updateMessageAction(message)
                } else {
                   this.addMessageAction(message)
                }
                this.text = ''
                this.id = ''
            } 
        }
    }
</script>

<style>
</style>