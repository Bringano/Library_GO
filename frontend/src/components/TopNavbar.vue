<script setup>
import { RouterLink } from 'vue-router'
</script>

<template>
    <div class="navbar container mx-auto">
        <div class="navbar-start">
            <div class="dropdown">
                <label tabindex="0" class="btn btn-ghost lg:hidden">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M4 6h16M4 12h8m-8 6h16" />
                    </svg>
                </label>
                <ul tabindex="0"
                    class="menu menu-compact dropdown-content mt-3 p-2 shadow bg-base-100 rounded-box w-52">
                    <li><RouterLink to="/search">Search</RouterLink></li>
                    <li><RouterLink to="/classifications">Classifications</RouterLink></li>
                    <template v-if="$auth.hasRole('USER')">
                        <li><RouterLink to="/user/loans">My Books</RouterLink></li>
                    </template>
                    <template v-if="$auth.hasRole('ADMIN')">
                        <li tabindex="0">
                            <a class="justify-between">
                                Administration
                                <svg class="fill-current" xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                                    viewBox="0 0 24 24">
                                    <path d="M8.59,16.58L13.17,12L8.59,7.41L10,6L16,12L10,18L8.59,16.58Z" />
                                </svg>
                            </a>
                            <ul class="p-2 bg-base-200 z-50">
                                <li><RouterLink to="/admin/loans">Handle loans</RouterLink></li>
                                <li><RouterLink to="/admin/overdue">Late books</RouterLink></li>
                            </ul>
                        </li>
                    </template>
                </ul>
            </div>
            <div class="normal-case text-xl flex items-center">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="mx-2 hidden lg:inline-block"
                    viewBox="0 0 16 16">
                    <path
                        d="M1 2.828c.885-.37 2.154-.769 3.388-.893 1.33-.134 2.458.063 3.112.752v9.746c-.935-.53-2.12-.603-3.213-.493-1.18.12-2.37.461-3.287.811V2.828zm7.5-.141c.654-.689 1.782-.886 3.112-.752 1.234.124 2.503.523 3.388.893v9.923c-.918-.35-2.107-.692-3.287-.81-1.094-.111-2.278-.039-3.213.492V2.687zM8 1.783C7.015.936 5.587.81 4.287.94c-1.514.153-3.042.672-3.994 1.105A.5.5 0 0 0 0 2.5v11a.5.5 0 0 0 .707.455c.882-.4 2.303-.881 3.68-1.02 1.409-.142 2.59.087 3.223.877a.5.5 0 0 0 .78 0c.633-.79 1.814-1.019 3.222-.877 1.378.139 2.8.62 3.681 1.02A.5.5 0 0 0 16 13.5v-11a.5.5 0 0 0-.293-.455c-.952-.433-2.48-.952-3.994-1.105C10.413.809 8.985.936 8 1.783z" />
                </svg>
                The Library
            </div>
        </div>
        <div class="navbar-center hidden lg:flex">
            <ul class="menu menu-horizontal p-0">
                <li><RouterLink to="/search">Search</RouterLink></li>
                <li><RouterLink to="/classifications">Classifications</RouterLink></li>
                <template v-if="$auth.hasRole('USER')">
                    <li><RouterLink to="/user/loans">My Books</RouterLink></li>
                </template>
                <template v-if="$auth.hasRole('ADMIN')">
                    <li tabindex="0">
                        <a>
                            Administration
                            <svg class="fill-current" xmlns="http://www.w3.org/2000/svg" width="20" height="20"
                                viewBox="0 0 24 24">
                                <path d="M7.41,8.58L12,13.17L16.59,8.58L18,10L12,16L6,10L7.41,8.58Z" />
                            </svg>
                        </a>
                        <ul class="p-2 bg-base-200 z-50">
                            <li><RouterLink to="/admin/loans">Handle loans</RouterLink></li>
                            <li><RouterLink to="/admin/overdue">Late books</RouterLink></li>
                        </ul>
                    </li>
                </template>
            </ul>
        </div>
        <div class="navbar-end gap-x-2">
            <template v-if="!$auth.isAuthenticated()">
                <RouterLink to="/register" class="btn">Register</RouterLink>
                <RouterLink to="/login" class="btn btn-primary">Login</RouterLink>
            </template>
            <template v-else>
                <a class="btn btn-outline" @click="logout">Logout</a>
            </template>
        </div>
    </div>
</template>

<script>
import { mapStores } from 'pinia'
import { useAuthStore } from '@/stores/auth.js'

/**
 * Component for the top navigation bar. It is not intended to use multiple
 * times in the same page.
 */
export default {
    name: 'TopNavbar',
    computed: {
        ...mapStores(useAuthStore),
    },
    methods: {
        async logout() {
            this.authStore.logout()
            this.$router.push("/")
        },
    },
}
</script>
