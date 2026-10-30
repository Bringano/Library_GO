<script setup>
import { RouterView } from 'vue-router'
import TopNavbar from './components/TopNavbar.vue';
</script>

<template>
    <header class="w-full mx-auto bg-base-200">
        <TopNavbar />
    </header>
    <div class="container mx-auto">
        <RouterView />
    </div>
</template>
