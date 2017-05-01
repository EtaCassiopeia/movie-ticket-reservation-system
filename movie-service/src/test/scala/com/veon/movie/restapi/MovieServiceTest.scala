package com.veon.movie.restapi

import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import com.veon.movie.restapi.model.MovieEntity
import io.circe.generic.auto._
import org.scalatest.concurrent.ScalaFutures

import scala.util.Random

class MovieServiceTest extends BaseServiceTest with ScalaFutures {

  import moviesService._

  trait Context {
    val testMovies = movieList(5)
    val route = httpService.moviesRouter.route
  }

  "Movies service" should {

    "retrieve movie by imdbId and screenId" in new Context {
      val testMovie = testMovies(4)
      Get(s"/api/movies/${testMovie.imdbId}/${testMovie.screenId}") ~> route ~> check {
        responseAs[MovieEntity] should be(testMovie)
      }
    }

    "create movie and retrieve it" in new Context {

      val movieTitle = Random.alphanumeric.take(10).mkString
      val imdbId = Random.alphanumeric.take(10).mkString
      val screenId = Random.alphanumeric.take(10).mkString
      val availableSeats = Random.nextInt(100)

      val requestEntity = HttpEntity(MediaTypes.`application/json`,
        s"""{
         "imdbId" : "${imdbId}",
         "screenId" : "${screenId}",
         "availableSeats" : ${availableSeats},
         "title" : "${movieTitle}"
        }"""
      )

      Post(s"/api/movies", requestEntity) ~> route ~> check {
        whenReady(getMovieByImdbIdScreenId(imdbId, screenId)) { result =>
          result.get.title should be(movieTitle)
        }
      }
    }
  }
}
