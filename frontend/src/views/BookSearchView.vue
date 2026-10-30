<template>
    <section class="flex justify-center my-2">
        <form @submit.prevent="search" class="max-w-md w-full bg-base-300 rounded p-6 space-y-4">
            <input type="text" v-model="title" placeholder="Title" class="input input-bordered w-full max-w-md">
            <input type="text" v-model="author" placeholder="Author" class="input input-bordered w-full max-w-md">
            <input type="text" v-model="isbn" placeholder="ISBN" class="input input-bordered w-full max-w-md">

            <div :class="[v$.isbn.$errors.length === 0 ? 'hidden' : 'block']" style="margin-top: 0;">
                <div class="text-xs text-error" v-for="error of v$.isbn.$errors" :key="error.$uid"
                    style="margin-top: 0;">
                    <div class="error-msg">{{ error.$message }}</div>
                </div>
            </div>

            <input type="submit" value="Search" class="btn btn-primary">
        </form>
    </section>

    <section class="errors overflow-x-auto my-4 flex flex-col items-center" v-if="errors.length > 0">
        <div v-for="err in errors" :key="err">
            {{ err }}
        </div>
    </section>

    <section class="found-items overflow-x-auto my-4" v-if="books.length > 0">
        <table class="table w-full">
            <caption class="text-xl my-4">Matching books</caption>
            <thead>
                <tr>
                    <th class="rounded-l">Title</th>
                    <th>Author</th>
                    <th class="hidden lg:table-cell">ISBN</th>
                    <th class="hidden lg:table-cell">Class</th>
                    <th class="rounded-r">#</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="book in books" :key="book.isbn">
                    <td class="text-ellipsis overflow-hidden max-w-xs">{{ book.title }}</td>
                    <td class="text-ellipsis overflow-hidden max-w-xs">{{ book.author }}</td>
                    <td class="hidden lg:table-cell">{{ book.isbn }}</td>
                    <td class="hidden lg:table-cell">{{ book.ddsClassification.classification }}</td>
                    <td>{{ book.count }}</td>
                </tr>
            </tbody>
        </table>
    </section>
</template>

<script>
import useVuelidate from '@vuelidate/core'
import { minLength } from '@vuelidate/validators'


export default {
    name: 'BookSearchView',
    setup() {
        return { v$: useVuelidate() }
    },
    data() {
        return {
            isbn: "",
            author: "",
            title: "",
            books: [],
            errors: [],
        }
    },
    validations() {
        return {
            isbn: { minLength: minLength(5) },
            author: { minLength: minLength(2) },
            title: { minLength: minLength(3) },
        }
    },
    methods: {
        async search() {
            this.errors.length = 0

            const valid = await this.v$.$validate()
            if (valid) {
                const eIsbn = encodeURIComponent(this.isbn)
                const eAuthor = encodeURIComponent(this.author)
                const eTitle = encodeURIComponent(this.title)

                const url = `/api/books?isbn=${eIsbn}&author=${eAuthor}&title=${eTitle}`
                const resp = await fetch(url)

                if (!resp.ok) {
                    return
                }

                const books = await resp.json()
                if (books && books.length) {
                    this.books = books;
                }
                else {
                    this.books.length = 0
                    this.errors = ["No books found"]
                }
            }
        },
    },
}
</script>