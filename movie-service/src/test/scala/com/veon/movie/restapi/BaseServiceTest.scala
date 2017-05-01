package com.veon.movie.restapi

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.veon.common.utils.DatabaseService
import com.veon.movie.restapi.http.HttpService
import com.veon.movie.restapi.model.MovieEntity
import com.veon.movie.restapi.service.MoviesService
import com.veon.movie.restapi.utils.InMemoryPostgresStorage._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import org.scalatest._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Random

trait BaseServiceTest extends WordSpec with Matchers with ScalatestRouteTest with FailFastCirceSupport {

  dbProcess.getProcessId

  private val databaseService = new DatabaseService(jdbcUrl, dbUser, dbPassword)

  val moviesService = new MoviesService(databaseService)
  val httpService = new HttpService(moviesService)

  def movieList(size: Int): Seq[MovieEntity] = {
    val savedMovies = (1 to size).map { _ =>
      MovieEntity(Some(Random.nextLong()),
        Random.alphanumeric.take(10).mkString,
        Random.alphanumeric.take(10).mkString,
        Random.alphanumeric.take(10).mkString,
        Random.nextInt(100))
    }.map(moviesService.createMovie)

    Await.result(Future.sequence(savedMovies), 10.seconds)
  }

}