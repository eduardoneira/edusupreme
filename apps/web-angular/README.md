# Edusupreme Web Angular

Angular frontend shell for the Edusupreme tournament platform.

This app currently provides the M0 application frame: primary navigation, a tournament operations dashboard, and a lazy route boundary ready for future feature screens.

## Prerequisites

- Node.js LTS or newer.
- npm 11 or newer.

## Install

From this directory:

```bash
npm install
```

## Run Locally

```bash
npm start
```

The app starts at `http://localhost:4200/`.

## Build

```bash
npm run build
```

Production build artifacts are written to `dist/`.

## Lint

```bash
npm run lint
```

ESLint is configured through `angular-eslint` for TypeScript and Angular templates.

## Test

```bash
npm test
```

Unit tests run through the Angular CLI test builder.

## Feature Routes

The shell uses standalone Angular routing in `src/app/app.routes.ts`. Add future feature screens as lazy routes and link them from the navigation in `src/app/app.ts`.
