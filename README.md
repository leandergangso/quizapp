# Quiz App

Quiz App is a simple quiz web-application, that allows users to
answer questions and get the result at the end.  
All correct options give one point and there is no penalty for choosing wrong.  
You can currently create quistions with 3 supported input types: 'text', 'checkbox' or 'radio'.

## Requirements

There are some requirements you need before you can run this locally.  
You will need these programs installed on your computer:

- node & npm/pnpm/yarn
- java
- gradle
- kotlin 

Note java, gradle and kotlin can be installed with (sdkman.io) `sdk install {package}`

## Run for development

### Frontend (svelte w/kit)

Follow the steps bellow to run the frontend.

- Open a terminal
- Navigate into `webapp/`
- Run `cp .env.example .env` to use default env
- Run `pnpm i` to install packages
- Run `pnpm dev` to run in development mode

The frontend runs on port:5050 by default.

### Backend (kotlin w/gradle)

Follow the steps bellow to run the backend.

- Open a termianl
- Navigate into `server/`
- Run `gradle run` will build if needed

The backend runs on port:8080 by default.

### Backend w/autoreload

This allows you to watch for file changes in the project.  
Any change will trigger a rebuild and rerun the application.

- Open two terminals
- Navigate into `server/` 
- In terminal.1 run `gradle -t build` (this will watch your directory for changes)
- In terminal.2 run `gradle run` (runs your application and re-run when detecting new build)

## Build for production

...TO BE CONTINUE...
