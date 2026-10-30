<template>
    <div class="bg-slate-800 bg-opacity-50 flex justify-center items-center absolute top-0 right-0 bottom-0 left-0 hidden"
        ref="modal">
        <div class="bg-white px-16 py-14 rounded-md text-center">
            <h2 class="text-xl mb-8 font-bold text-slate-500 max-h-96 overflow-y-auto">
                <slot></slot>
            </h2>
            <button :class="['btn', 'btn-warning', 'mx-4', 'w-24', onlyOk ? 'hidden' : '']" ref="cancel">Cancel</button>
            <button class="btn btn-primary w-24" ref="ok">Ok</button>
        </div>
    </div>
</template>

<script>
/**
 * Modal "popup" box for displaying messages. Will by default have a Ok and
 * a Cancel button.
 */
export default {
    name: "ModalBox",
    props: {
        /**
         * Set to show only an Ok button and no Cancel button.
         */
        onlyOk: Boolean,
    },
    methods: {
        /**
         * Show the modal box and return a promise that will resolve when
         * any of the buttons are clicked. Will resolve to true if the
         * ok button is clicked and to false if the cancel button is
         * clicked.
         */
        show() {
            const modal = this.$refs.modal
            const okButton = this.$refs.ok
            const cancelButton = this.$refs.cancel

            modal.classList.add("block")
            modal.classList.remove("hidden")

            if (this.onlyOk) {
                this.$refs.ok.focus()
            }
            else {
                this.$refs.cancel.focus()
            }

            return new Promise((resolve) => {
                function reset() {
                    modal.classList.add("hidden")
                    modal.classList.remove("block")
                    cancelButton.removeEventListener('click', onCancel)
                    okButton.removeEventListener('click', onOk)
                }

                function onCancel() {
                    reset()
                    resolve(false)
                }

                function onOk() {
                    reset()
                    resolve(true)
                }

                cancelButton.addEventListener('click', onCancel)
                okButton.addEventListener('click', onOk)
            })
        },
    },
}
</script>