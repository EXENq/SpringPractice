<template>
    <v-card class="my-2">
        <v-card-title>
            <v-avatar 
                v-if="message.author && message.author.userpic"
                size="36px"
            >
                <img
                    :src="message.author.userpic"
                    :alt="message.author.name"
                >
            </v-avatar>
            <v-avatar 
                color="indigo" 
                v-else 
                size="36px"
            >
                <v-icon dark>
                    mdi-account-circle
                </v-icon>
            </v-avatar>
            <span calss="pl-3">{{ authorName }}</span>
        </v-card-title>
        <v-card-text class="text--primary pt-3">
            {{ message.text }}
        </v-card-text>
        <media v-if="message.link" :message="message"></media>
        <v-card-actions>
            <v-btn rounded small depressed value="Edit" @click="edit">Edit</v-btn>
            <v-btn small icon @click="del">
                <v-icon>delete</v-icon>
            </v-btn>
        </v-card-actions>
        <comment-list
            :comments="message.comments"
            :message-id="message.id"
        ></comment-list>
    </v-card>
</template>

<script>
    import { mapActions } from "vuex"
    import Media from 'components/media/Media.vue'
    import CommentList  from "components/comments/CommentList.vue";

    export default{
        props: ['message', 'editMessage'],
        components: { CommentList, Media },
        computed: {
            authorName() {
               return this.message.author ? this.message.author.name : 'unknown'
            } 
        },
        methods: {
            ...mapActions(['removeMessageAction']),
            edit() {
                this.editMessage(this.message)
            },
            del() {
                this.removeMessageAction(this.message)
            }
    }
}
</script>

<style>

</style>