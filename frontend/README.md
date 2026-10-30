# Library frontend

Frontend for the library application. This is a JavaScript project based on
the Vue 3 framework. It uses Tailwind CSS and daisyUI for styling and CSS
components.

This program is broken by design! It contains more bugs than an average ant 
hill! **DO NOT USE FOR ANYTHING OUT IN THE REAL WORLD**.

The Vue application will always assume that the web service API can be 
found under `/api`.

## Project Setup

```sh
npm install
```

### Compile and Hot-Reload for Development

```sh
npm run dev
```

### Compile and Minify for Production

```sh
npm run build
```

### Run Unit Tests with [Vitest](https://vitest.dev/)

```sh
npm run test:unit
```

### Run End-to-End Tests with [Cypress](https://www.cypress.io/)

```sh
npm run build
npm run test:e2e # or `npm run test:e2e:ci` for headless testing
```

### Lint with [ESLint](https://eslint.org/)

```sh
npm run lint
```
