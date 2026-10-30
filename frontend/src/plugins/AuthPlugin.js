import { useAuthStore } from '@/stores/auth.js'

export default {
    install: (app) => {
        const authStore = useAuthStore()

        app.config.globalProperties.$auth = {
            hasRole: (role) => authStore.hasRole(role),
            roles: () => [...authStore.roles],
            isAuthenticated: () => authStore.hasRole(),
        }
    },
}
