package com.veon.movie.restapi.http.routes

import akka.http.scaladsl.server.Directives._
import com.veon.movie.restapi.model.MovieEntity
import com.veon.movie.restapi.service.MoviesService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext

class MoviesServiceRoute(moviesService: MoviesService
                        )(implicit executionContext: ExecutionContext) extends FailFastCirceSupport {

  import moviesService._

  val route = pathPrefix("movies") {
    post {
      entity(as[MovieEntity]) { movie =>
        complete(createMovie(movie).map(_.asJson))
      }
    } ~
      pathPrefix(Segment / Segment) { (imdbId, screenId) =>
        pathEndOrSingleSlash {
          get {
            complete(getMovieByImdbIdScreenId(imdbId, screenId).map(_.asJson))
          }
        }
      }
  }
}