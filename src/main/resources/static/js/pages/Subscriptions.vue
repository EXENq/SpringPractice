<template>
   <v-container>
        <v-layout justify-space-around>
            <v-list>
                <v-list-item
                    v-for="item in subscriptions"
                >
                    <user-link
                        :user="item.subscriber"
                        size="24"
                    ></user-link>
                    <v-btn
                        @click="changeUserSubscriptionStatus(item.subscriberId)"
                    >
                        {{item.active ? "Dismiss" : "Approve"}}
                    </v-btn>
                </v-list-item>
            </v-list>
        </v-layout>
    </v-container>

</template>

<script>
    import UserLink from 'components/UserLink.vue'
    import profileApi from 'api/profile.js';

    export default {
        name: 'Subscriptions',
        components: {
            UserLink,
        },
        data() {
            return {
               subscriptions: []
            }
        },
        methods: {
            async changeUserSubscriptionStatus(subscriberId) {
                await profileApi.changeUserSubscriptionStatus(subscriberId)

                const subscriptionIndex = this.subscriptions.findIndex(item => item.subscriber.id === subscriberId)
                const subscription = this.subscriptions[subscriptionIndex]
                this.subscriptions = [
                    ...this.subscriptions.slice(0, subscriptionIndex),
                    {
                        ...subscription,
                        active: !subscription.active
                    },
                    ...this.subscriptions.slice(subscriptionIndex + 1)
                ]
            }
        },
        async beforeMount () {
            const resp = await profileApi.subscriberList(this.$store.state.profile.id)
            this.subscriptions = await resp.json()
        },
    }
</script>

<style scoped>

</style>