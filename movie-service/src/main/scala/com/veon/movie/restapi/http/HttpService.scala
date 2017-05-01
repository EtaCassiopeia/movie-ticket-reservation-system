package com.veon.movie.restapi.http

import akka.http.scaladsl.server.Directives._
import com.veon.common.utils.CorsSupport
import com.veon.movie.restapi.http.routes.MoviesServiceRoute
import com.veon.movie.restapi.service.MoviesService

import scala.concurrent.ExecutionContext

class HttpService(moviesService: MoviesService
                 )(implicit executionContext: ExecutionContext) extends CorsSupport {

  val moviesRouter = new MoviesServiceRoute( moviesService)

  val routes =
    pathPrefix("api") {
      corsHandler {
        moviesRouter.route
      }
    }
}