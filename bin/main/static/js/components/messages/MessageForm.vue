<template>
    <v-container>
        <v-row>
            <v-col><v-text-field @key.enter="save" label="Write your message!" outlined v-model="text" /></v-col>
            <v-col><v-btn @click="save">
                Save
            </v-btn></v-col>
        </v-row>
    </v-container>
</template>

<script>
    import { mapActions } from 'vuex'
    export default {
        props: ['messageAttr'],
        data() {
            return {
                text: '',
                id: null
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
                    id: this.id,
                    text: this.text
                }
                if (this.id) {
                    this.updateMessageAction(message)
                } else {
                    this.addMessageAction(message)
                }
                this.text = ''
                this.id = null
            }
        }
    }
</script>

<style>
</style>