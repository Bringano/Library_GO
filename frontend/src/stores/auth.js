import { defineStore } from "pinia";
import { useSessionStorage } from '@vueuse/core'

export const useAuthStore = defineStore("auth", {
    state: () => ({
        isAuthenticated: useSessionStorage('is-authenticated', false),
        roles: useSessionStorage('roles', []),
    }),
    actions: {
        async login(username, password) {
            const data = { username, password };
            const resp = await fetch("/api/login", {
                method: "POST",
                cache: "no-cache",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(data),
            });

            if (resp.status !== 200) {
                this.isAuthenticated = false;
                return false;
            }

            this.isAuthenticated = true;
            const roles = await resp.json();
            this.roles = roles;
            return true;
        },
        async logout() {
            fetch("/api/logout")
            this.isAuthenticated = false
            this.roles = []
        },
        hasRole(role) {
            if (!this.isAuthenticated) {
                return false
            }

            if (role) {
                return this.roles.includes(role)
            }
            
            return true
        },
    },
});
