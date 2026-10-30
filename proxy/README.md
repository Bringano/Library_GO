# Library proxy

This is a proxy to make sure that the frontend and backend can be accessed
trough the same origin. It will be available at http://localhost:8282.

If you have changed any of the ports for the backend or the frontend
you need to edit `index.js` and provide the correct port numbers.

Even though they are really two different servers, by accessing them
through the proxy the web browser will see them as one server (one 
origin) and relax its security policies.

```
              │ ▲            
              │ │            
              │ │            
         ┌────▼─┴────┐        
         │           │        
         │   PROXY   │        
         │           │        
         └┬─▲─────▲─┬┘        
          │ │     │ │         
          │ │     │ │         
┌─────────▼─┴┐   ┌┴─▼────────┐
│            │   │           │
│  BACKEND   │   │ FRONTEND  │
│            │   │           │
└────────────┘   └───────────┘
```

## Project Setup

```sh
npm install
```

## Running

```sh
node index.js
```
