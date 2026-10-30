# Library application

Do not use this code for any real application! This is a small web application 
intended for use with a course in testing and security at Yrgo.

This program is broken by design! It contains more bugs than an average ant 
hill! **DO NOT USE FOR ANYTHING OUT IN THE REAL WORLD**.

## Parts of the application

This application is comprised of three modules. The backend is a Java 
application that handles talking to a MySQL database and exposes a simple
web service. The frontend is a Vue webb application. The proxy is a simple
proxy for use in development to make sure that the Vue application and
web service are served from the same origin to prevent problems with SOP and
CORS. The proxy will place the web service api behind the URL `/api`.

In an actual production environment the proxy will be replaced by some kind 
of API gateway.

## Running the application

To run the application start the individual parts (have a look at their 
READMEs) and access it through the proxy.

Running a local dev environment:

```
# in the backend folder run
mvn clean jooby:run

# in the frontend folder run
npm run dev

# in the proxy folder run
node index.js
```

Access the running application at http://localhost:8282.
