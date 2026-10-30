<template>
    <div class="flex flex-col lg:flex-row gap-4 my-2 w-full justify-center">
        <ModalBox ref="question"><span v-html="question"></span></ModalBox>
        <ModalBox ref="alert" only-ok>{{ question }}</ModalBox>
        <ModalBox ref="alertLend" only-ok>
            The lend form is not properly filled in.<br>
            <span v-if="bookIdError.length > 0">Book ID: {{ bookIdError }}<br></span>
            <span v-if="userIdError.length > 0">User ID: {{ userIdError }}</span>
        </ModalBox>
        <ModalBox ref="alertReturn" only-ok>
            The return form is not properly filled in.<br>
            <span v-if="bookIdError.length > 0">Book ID: {{ bookIdError }}<br></span>
            <span v-if="bookIsbnError.length > 0">ISBN: {{ bookIsbnError }}</span>
        </ModalBox>

        <section class="flex justify-center">
            <form @submit.prevent="lendBook" class="max-w-md w-full bg-base-300 rounded p-6 space-y-4">
                <h2 class="font-bold">Lend out a book</h2>
                <input type="number" v-model="loanBookId" placeholder="Book ID"
                    class="input input-bordered w-full max-w-md">
                <input type="number" v-model="loanUserId" placeholder="User ID"
                    class="input input-bordered w-full max-w-md">

                <input type="submit" value="Lend book" class="btn btn-primary">
            </form>
        </section>

        <section class="flex justify-center">
            <form @submit.prevent="returnBook" class="max-w-md w-full bg-base-300 rounded p-6 space-y-4">
                <h2 class="font-bold">Return a book</h2>
                <input type="number" v-model="returnBookId" placeholder="Book ID"
                    class="input input-bordered w-full max-w-md">
                <input type="text" v-model="returnBookIsbn" placeholder="ISBN"
                    class="input input-bordered w-full max-w-md">

                <input type="submit" value="Return book" class="btn btn-primary">
            </form>
        </section>
    </div>
</template>

<script>
import useVuelidate from '@vuelidate/core'
import { numeric, required } from '@vuelidate/validators'
import ModalBox from '@/components/ModalBox.vue'

export default {
    name: 'AdminLoanView',
    setup() {
        return { v$: useVuelidate() }
    },
    components: {
        ModalBox,
    },
    data() {
        return {
            loanBookId: "",
            loanUserId: "",
            returnBookId: "",
            returnBookIsbn: "",
            bookIdError: "",
            bookIsbnError: "",
            userIdError: "",
            question: "",
        }
    },
    validations() {
        return {
            loanBookId: { numeric, required },
            loanUserId: { numeric, required },
            returnBookId: { numeric, required },
            returnBookIsbn: { required },
        }
    },
    methods: {
        async lendBook() {
            // do not check return value since validate validates 
            // both forms at once (unfortunately)
            await this.v$.$validate()
            if (this.v$.loanBookId.$invalid || this.v$.loanUserId.$invalid) {
                this.bookIdError = this.v$.loanBookId.$errors.map(x => x.$message).join(' ')
                this.userIdError = this.v$.loanUserId.$errors.map(x => x.$message).join(' ')
                await this.$refs.alertLend.show()
                return
            }

            const bookPromise = fetch(`/api/admin/books/${this.loanBookId}`)
            const userPromise = fetch(`/api/admin/users/${this.loanUserId}`)

            // TODO: check ok from resp
            const [bookResp, userResp] = await Promise.all([bookPromise, userPromise])
            const [book, user] = await Promise.all([bookResp.json(), userResp.json()])

            if (book == null || !book.available || user == null) {
                const bookFoundError = book?.available ? "" : "Book is unavailable"
                this.bookIdError = book ? bookFoundError : "Book not found"
                this.userIdError = user ? "" : "User not found"
                await this.$refs.alertLend.show()
                return
            }

            this.question = `Do you wish to lend out book ${book.edition.title} to ${user.realname} (${user.name})?`
            const res = await this.$refs.question.show()
            if (res) {
                const data = {
                    book: this.loanBookId,
                    user: this.loanUserId,
                }

                const loanResp = await fetch("/api/admin/loan/lend", {
                    method: "POST",
                    cache: "no-cache",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(data),
                })

                if (!loanResp.ok || (await loanResp.json()) !== true) {
                    this.question = "The loan did not succeed."
                    await this.$refs.alert.show()
                }
                else {
                    this.loanBookId = ""
                }
            }
        },
        async returnBook() {
            // do not check return value since validate validates 
            // both forms at once (unfortunately)
            await this.v$.$validate()
            if (this.v$.returnBookId.$invalid || this.v$.returnBookIsbn.$invalid) {
                this.bookIdError = this.v$.returnBookId.$errors.map(x => x.$message).join(' ')
                this.bookIsbnError = this.v$.returnBookIsbn.$errors.map(x => x.$message).join(' ')
                await this.$refs.alertReturn.show()
                return
            }

            // TODO: check ok
            const bookResp = await fetch(`/api/admin/books/${this.returnBookId}`)
            const book = await bookResp.json()

            if (book == null || book.edition.isbn !== this.returnBookIsbn) {
                this.bookIdError = book ? "" : "Book not found or unavailable"
                this.bookIsbnError = book.edition.isbn === this.returnBookIsbn ? "" : `Does not match the book (${book.edition.isbn})`
                await this.$refs.alertReturn.show()
                return
            }

            this.question = `Do you wish to return book ${book.edition.title}`
            const res = await this.$refs.question.show()
            if (res) {
                const returnResp = await fetch("/api/admin/loan/return", {
                    method: "POST",
                    cache: "no-cache",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(book.id),
                })

                if (!returnResp.ok || (await returnResp.json()) !== true) {
                    this.question = "Returning the book did not succeed"
                    await this.$refs.alert.show()
                }
                else {
                    this.returnBookId = ""
                    this.returnBookIsbn = ""
                }
            }
        },
    },
}
</script>