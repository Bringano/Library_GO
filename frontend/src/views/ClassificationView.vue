<template>
    <ModalBox ref="bookAlert" only-ok class="z-50">
        <section class="overflow-x-auto my-4" v-if="books.length > 0">
            <table class="table table-compact w-full">
                <caption class="text-xl my-4">Books in library</caption>
                <thead>
                    <tr>
                        <th class="rounded-l">Title</th>
                        <th>Author</th>
                        <th class="hidden lg:table-cell rounded-r">ISBN</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="book in books" :key="book.isbn">
                        <td class="text-ellipsis overflow-hidden max-w-xs">{{ book.title }}</td>
                        <td class="text-ellipsis overflow-hidden max-w-xs">{{ book.author }}</td>
                        <td class="hidden lg:table-cell">{{ book.isbn }}</td>
                    </tr>
                </tbody>
            </table>
        </section>
    </ModalBox>

    <section class="overflow-x-auto my-4" v-if="classifications.length > 0">
        <table class="table w-full hover">
            <caption class="text-xl my-4">Classifications</caption>
            <thead>
                <tr>
                    <th class="rounded-l">Code</th>
                    <th class="rounded-r">Classification</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="clazz in classifications" :key="clazz.code" @click="loadBooks(clazz.code)">
                    <td>{{ clazz.code }}</td>
                    <td class="text-ellipsis overflow-hidden max-w-xs">{{ clazz.classification }}</td>
                </tr>
            </tbody>
        </table>
    </section>
</template>

<script>
import ModalBox from '../components/ModalBox.vue';

export default {
    name: "ClassificationView",
    components: { ModalBox },
    data() {
        return {
            classifications: [],
            books: [],
        };
    },
    created() {
        this.getAvailableClassifications();
    },
    methods: {
        async getAvailableClassifications() {
            const resp = await fetch("/api/classifications")
            if (!resp.ok) {
                return;
            }
            const classifications = await resp.json();
            if (classifications && classifications.length) {
                this.classifications = classifications;
            }
        },
        async loadBooks(classification) {
            const eClass = encodeURIComponent(classification)
            const resp = await fetch(`/api/books?classification=${eClass}`)
            if (resp.ok) {
                this.books = await resp.json()
                await this.$refs.bookAlert.show()
                this.books = []
            }
        },
    },
}
</script>