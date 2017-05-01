package com.veon.movie.restapi.model.db

import com.veon.common.utils.DatabaseService
import com.veon.movie.restapi.model.MovieEntity

trait MovieEntityTable {

  protected val databaseService: DatabaseService
  import databaseService.driver.api._

  class Movies(tag: Tag) extends Table[MovieEntity](tag, "movies") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def imdbId = column[String]("imdbId")
    def screenId = column[String]("screenId")
    def title = column[String]("title")
    def availableSeats = column[Int]("availableSeats")

    def * = (id, imdbId, screenId,title,availableSeats) <> ((MovieEntity.apply _).tupled, MovieEntity.unapply)
  }

  protected val movies = TableQuery[Movies]

}