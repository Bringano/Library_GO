import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterUserView from '../views/RegisterUserView.vue'
import AboutView from '../views/AboutView.vue'
import BookSearchView from '../views/BookSearchView.vue'
import ClassificationView from '../views/ClassificationView.vue'
import UserLoanView from '../views/user/LoanView.vue'
import AdminLoanView from '../views/admin/LoanView.vue'
import AdminOverdueView from '../views/admin/OverdueView.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: HomeView,
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView,
        },
        {
            path: '/register',
            name: 'register',
            component: RegisterUserView,
        },
        {
            path: '/about',
            name: 'about',
            component: AboutView,
        },
        {
            path: '/search',
            name: 'search',
            component: BookSearchView,
        },
        {
            path: '/classifications',
            name: 'classifications',
            component: ClassificationView,
        },
        {
            path: '/user/loans',
            name: 'userloans',
            component: UserLoanView,
        },
        {
            path: '/admin/loans',
            name: 'adminloans',
            component: AdminLoanView,
        },
        {
            path: '/admin/overdue',
            name: 'adminoverdue',
            component: AdminOverdueView,
        },
    ],
})

export default router
