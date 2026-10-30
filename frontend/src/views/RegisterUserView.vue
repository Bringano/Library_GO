<template>
    <ModalBox ref="alert" only-ok>Unable to register user!</ModalBox>

    <section class="flex justify-center my-2">
        <form @submit.prevent="register" class="max-w-md w-full bg-base-300 rounded p-6 space-y-4">
            <input type="text" v-model="username" placeholder="Username"
                :class="['input', 'input-bordered', 'w-full', 'max-w-md', !v$.username.$error ? 'border-green-500' : 'border-red-500']">
            <input type="text" v-model="realname" placeholder="Real Name"
                :class="['input', 'input-bordered', 'w-full', 'max-w-md', !v$.realname.$error ? 'border-green-500' : 'border-red-500']">
            <input type="password" v-model="v$.password.$model" placeholder="Password"
                :class="['input', 'input-bordered', 'w-full', 'max-w-md', v$.password.$error ? 'border-red-500' : '']">
            <input type="password" v-model="v$.password2.$model" placeholder="Verify Password"
                :class="['input', 'input-bordered', 'w-full', 'max-w-md', v$.password2.$error ? 'border-red-500' : '']">
            <input type="submit" value="Register" class="btn btn-primary">
        </form>
    </section>
</template>

<script>
import ModalBox from '../components/ModalBox.vue'
import useVuelidate from '@vuelidate/core'
import { helpers, required, minLength, sameAs } from '@vuelidate/validators'
import { watchDebounced } from '@vueuse/core'

const userNameValidator = helpers.regex(/^[a-zA-Z0-9@._-]{4,}$/)

export default {
    name: 'RegisterUserView',
    components: {
        ModalBox,
    },
    setup() {
        return { v$: useVuelidate() }
    },
    data() {
        return {
            username: "",
            realname: "",
            password: "",
            password2: "",
            isUserNameAvailable: 0,
        }
    },
    validations() {
        return {
            username: { userNameValidator, availableValidator: () => this.availableValidator() },
            realname: { required },
            password: { required, minLength: minLength(8) },
            password2: { required, sameAs: sameAs(this.password) },
        }
    },
    created() {
        watchDebounced(
            () => this.username,
            async () => {
                const resp = await fetch(`/api/register/available?name=${this.username}`)
                if (resp.ok) {
                    const json = await resp.json()
                    this.isUserNameAvailable = json
                }
                else {
                    this.isUserNameAvailable = false;
                }
                this.v$.username.$touch()
            },
            { debounce: 500, maxWait: 1000 }
        )
    },
    mounted() {
        this.v$.username.$touch()
    },
    methods: {
        availableValidator() {
            return this.isUserNameAvailable
        },
        async register() {
            const valid = this.v$.$validate()
            if (valid) {
                const data = {
                    name: this.username,
                    realName: this.realname,
                    password: this.password,
                }

                const resp = await fetch("/api/register",
                    {
                        method: "POST",
                        cache: "no-cache",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: JSON.stringify(data),
                    })

                if (!resp.ok || !(await resp.json())) {
                    await this.$refs.alert.show()
                    return
                }

                this.username = ""
                this.realname = ""
                this.password = ""
                this.password2 = ""

                this.$router.push("/login")
            }
        },
    },
}
</script>