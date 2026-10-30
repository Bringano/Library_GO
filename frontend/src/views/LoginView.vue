<template>
    <section class="flex justify-center my-2">
        <form @submit.prevent="login" class="max-w-md w-full bg-base-300 rounded p-6 space-y-4">
            <input type="text" v-model="username" placeholder="Username" class="input input-bordered w-full max-w-md">
            <input type="password" v-model="password" placeholder="Password"
                class="input input-bordered w-full max-w-md">

            <input type="submit" value="Login" class="btn btn-primary">
        </form>
    </section>
</template>

<script>
import { mapStores } from 'pinia'
import { useAuthStore } from '@/stores/auth.js'

export default {
    name: 'LoginView',
    data() {
        return {
            username: "",
            password: "",
        }
    },
    computed: {
        ...mapStores(useAuthStore),
    },
    methods: {
        async login() {
            const auth = await this.authStore.login(this.username, this.password)
            if (auth) {
                this.$router.push('/')
            }
        },
    },
}
</script>