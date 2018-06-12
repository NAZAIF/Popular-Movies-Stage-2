# Popular-Movies App

In this App, you can discover the **most popular**, **top - rated** and **now - playing** movies. Ratings, reviews, trailers and everything about the movies you love is now at your fingertips.

The project is done as part of the **Android Developer Nanodegree** by _Udacity_.

This app uses **TheMovieDb** API 


## Features

The app:
- Present the user with a grid arrangement of movie posters upon launch.
- User can change the sort order via a setting as:
  * Most Popular
  * Top Rated
  * Now Playing
- Tap on a movie poster and transition to details screen with additional information such as:
  * Original Title
  * Movie poster Image thumbnail
  * A plot Synopsis
  * User Rating
  * Release date
- Users can view and play trailers ( either in the youtube app or a web browser).
- Users can read reviews of a selected movie.
- Users can mark a movie as a favorite in the details view by tapping a button(star) and its details can be viewed offline.


## Install 

- Request an API key from [TheMovieDb](https://www.themoviedb.org/)
- Clone this project.
- Place your key in `gradle.properties` file as the value for `API_KEY`.
- Run the project from Android Studio.
- Follow your favourite movies.


## Screenshots

![Movies sorted by Most Popular](/screenshots/pm7.jpg)
![setting](/screenshots/pm2.jpg)
![detail1](/screenshots/pm4.jpg)
![detail2](/screenshots/pm6.jpg)
![review](/screenshots/pm1.jpg)
![fav](/screenshots/pm5.jpg)


## What I learnt

- Fetched data from the Internet with theMovieDB API.
- Used adapters and custom list layouts to populate list views.
- Incorporate libraries([Picasso](http://square.github.io/picasso/),[Retrofit](http://square.github.io/retrofit/),[OKHttp](http://square.github.io/okhttp/),[Parceler](https://github.com/johncarl81/parceler)) to simplify the amount of code I need to write
- Built a fully featured application that looks and feels natural on the Android OS.

