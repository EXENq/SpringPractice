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
    import messagesApi from 'api/messages'
    export default {
        props: ['messages', 'messageAttr'],
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
            save() {
                const message = { 
                    text: this.text,
                    id: this.id 
                }
                if (this.id) {
                    messagesApi.update(message).then(result =>
                        result.json().then(data => {
                            const index = this.messages.findIndex(item => item.id == data.id)
                            this.messages.splice(index, 1, data)
                        })
                    )
                } else {
                    messagesApi.add(message).then(result =>
                        result.json().then(data => {
                            const index = this.messages.findIndex(item => item.id == data.id)

                            if (index > -1) {
                                this.messages.splice(index, 1 , data)
                            } else {
                                this.messages.push(data)
                            }
                        })
                    )
                }
                this.text = ''
                this.id = ''
            } 
        }
    }
</script>

<style>
</style>