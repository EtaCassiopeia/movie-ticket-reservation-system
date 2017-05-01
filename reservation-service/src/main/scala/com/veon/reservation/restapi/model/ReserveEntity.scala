package com.veon.reservation.restapi.model

case class ReserveEntity(id: Option[Long] = None, imdbId: String, screenId: String) {
  require(!imdbId.isEmpty, "imdbId.empty")
  require(!screenId.isEmpty, "screenId.empty")
}
