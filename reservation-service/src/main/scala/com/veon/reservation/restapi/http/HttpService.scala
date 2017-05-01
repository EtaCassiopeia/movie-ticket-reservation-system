package com.veon.reservation.restapi.http

import akka.http.scaladsl.server.Directives._
import com.veon.common.utils.CorsSupport
import com.veon.reservation.restapi.http.routes.ReserveServiceRoute
import com.veon.reservation.restapi.service.ReserveService

import scala.concurrent.ExecutionContext

class HttpService(reservesService: ReserveService
                 )(implicit executionContext: ExecutionContext) extends CorsSupport {

  val reservesRouter = new ReserveServiceRoute(reservesService)

  val routes =
    pathPrefix("api") {
      corsHandler {
        reservesRouter.route
      }
    }
}