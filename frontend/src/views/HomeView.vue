<template>
    <div class="hero h-full my-8 rounded-lg" style="background-image: url(/alfons-morales-YLSwjSy7stw-unsplash.jpg);">
        <div class="hero-overlay bg-opacity-60 rounded-lg"></div>
        <div class="hero-content text-center text-gray-200">
            <div class="max-w-md">
                <h1 class="mb-5 text-5xl font-bold">Welcome to<br>The Library</h1>
                <p class="mb-5">Here you can find some really nice books to read and enjoy.</p>
                <RouterLink to="/search" class="btn btn-primary">Find a book</RouterLink>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'HomeView',
}
</script>
