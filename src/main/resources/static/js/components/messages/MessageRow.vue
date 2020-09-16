<template>
    <v-card class="my-2">
        <v-card-title>
            ({{ message.id }})
        </v-card-title>
        <v-card-text class="text--primary">
            {{ message.text }}
        </v-card-text>
        <v-card-actions>
            <v-btn rounded small depressed value="Edit" @click="edit">Edit</v-btn>
            <v-btn small icon @click="del">
                <v-icon>delete</v-icon>
            </v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
    export default{
        props: ['message', 'editMessage', 'deleteMessage', 'messages'],
        methods: {
        edit() {
            this.editMessage(this.message)
        },
        del() {
            this.deleteMessage(this.message)
        }
    }
}
</script>

<style>

</style>