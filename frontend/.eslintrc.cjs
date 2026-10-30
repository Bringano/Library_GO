/* eslint-env node */
module.exports = {
    root: true,
    extends: ["plugin:vue/vue3-essential", "eslint:recommended"],
    env: {
        "vue/setup-compiler-macros": true,
    },
    overrides: [
        {
            files: ["cypress/e2e/**.{cy,spec}.{js,ts,jsx,tsx}"],
            extends: ["plugin:cypress/recommended"],
        },
    ],
    ignorePatterns: ["cypress.config.js"],
    rules: {
        indent: ["error", 4],
        "comma-dangle": [
            "error",
            {
                arrays: "always-multiline",
                objects: "always-multiline",
                imports: "never",
                exports: "never",
                functions: "never",
            },
        ],
    },
};
