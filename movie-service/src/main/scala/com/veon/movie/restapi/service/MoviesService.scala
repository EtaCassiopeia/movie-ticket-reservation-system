package com.veon.movie.restapi.service

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import com.veon.common.utils.DatabaseService
import com.veon.movie.restapi.model.MovieEntity
import com.veon.movie.restapi.model.db.MovieEntityTable
import slick.jdbc.meta.MTable

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class MoviesService(val databaseService: DatabaseService)
                   (implicit executionContext: ExecutionContext, actorSystem: ActorSystem) extends MovieEntityTable {

  import databaseService._
  import databaseService.driver.api._

  private val tables = List(movies)

  private val existing = db.run(MTable.getTables)
  private val f = existing.flatMap( v => {
    val names = v.map(mt => mt.name.name)
    val createIfNotExist = tables.filter( table =>
      !names.contains(table.baseTableRow.tableName)).map(_.schema.create)
    db.run(DBIO.sequence(createIfNotExist))
  })
  Await.result(f, Duration.Inf)

  implicit val log: LoggingAdapter = Logging(actorSystem, getClass)

  def getMovies(): Future[Seq[MovieEntity]] = {
   log.info("Get all movies")
    db.run(movies.result)
  }

  def getMovieById(id: Long): Future[Option[MovieEntity]] =
    db.run(movies.filter(_.id === id).result.headOption)

  def getMovieByImdbIdScreenId(imdbId: String, screenId: String): Future[Option[MovieEntity]] = {
    log.info(s"Receiving info for $imdbId $screenId")
    db.run(movies.filter(x => x.imdbId === imdbId && x.screenId === screenId).result.headOption)
  }

  def createMovie(movie: MovieEntity): Future[MovieEntity] = db.run(movies returning movies += movie)

  def deleteMovie(id: Long): Future[Int] = db.run(movies.filter(_.id === id).delete)

}