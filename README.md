# Radian6 Tag cloud API from Heroku

A sample application that demonstrates the use of Radian6's tag cloud API consumed by Heroku. The application
also uses d3 JS on the front-end to render the tag cloud.

## Environment variables

Setup the following environment variables before you attempt to run the application

1. R6_APP_KEY: Your Radian6 App Key
2. R6_PASSWORD_MD5: A MD5 hash of your radian6 password
3. R6_USERNAME: Your radian6 username
4. REDISTOGO_URL: Redis to go URL


## Running the application locally

Make sure your play framework install directory is on your path. Then navigate to the application directory and run the app with:

    $play run

## Running on heroku

    $ git push heroku master
    
That's about all it takes to deploy to Heroku.
