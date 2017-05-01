package com.veon.movie.restapi.model

case class MovieEntity(id: Option[Long] = None, imdbId: String, screenId: String, title: String, availableSeats: Int = 0) {
  require(!imdbId.isEmpty, "imdbId.empty")
  require(!screenId.isEmpty, "screenId.empty")
  require(!title.isEmpty, "title.empty")
}
