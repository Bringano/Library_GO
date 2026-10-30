<template>
    <section class="overflow-x-auto my-4" v-if="loans.length > 0">
        <table class="table w-full hover">
            <caption class="text-xl my-4">My Book Loans</caption>
            <thead>
                <tr>
                    <th class="rounded-l">ID</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th class="hidden lg:table-cell">ISBN</th>
                    <th class="rounded-r">Due Date</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="loan in loans" :key="loan.book.id">
                    <td>{{ loan.book.id }}</td>
                    <td class="text-ellipsis overflow-hidden max-w-xs">{{ loan.book.edition.title }}</td>
                    <td class="text-ellipsis overflow-hidden max-w-xs">{{ loan.book.edition.author }}</td>
                    <td class="hidden lg:table-cell">{{ loan.book.edition.isbn }}</td>
                    <td :class="{ 'bg-warning': loan.overdue }">{{ loan.dueDate }}</td>
                </tr>
            </tbody>
        </table>
    </section>
</template>

<script>
export default {
    name: 'UserLoanView',
    data() {
        return {
            loans: [],
        }
    },
    created() {
        this.getLoans()
    },
    methods: {
        async getLoans() {
            const resp = await fetch("/api/user/loans")

            if (!resp.ok) {
                return
            }

            const loans = await resp.json()
            if (loans && loans.length) {
                this.loans = loans;
            }
        },
    },
}
</script>