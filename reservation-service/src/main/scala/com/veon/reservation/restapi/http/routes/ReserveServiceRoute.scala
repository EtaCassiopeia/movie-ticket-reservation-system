package com.veon.reservation.restapi.http.routes

import akka.http.scaladsl.server.Directives._
import com.veon.reservation.restapi.model.ReserveEntity
import com.veon.reservation.restapi.service.ReserveService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext

class ReserveServiceRoute(moviesService: ReserveService
                         )(implicit executionContext: ExecutionContext) extends FailFastCirceSupport {

  import moviesService._

  val route = pathPrefix("reserves") {
    post {
      entity(as[ReserveEntity]) { reserveEntity =>
        complete(reserveSeat(reserveEntity).map(_.asJson))
      }
    } ~
      pathPrefix(Segment / Segment) { (imdbId, screenId) =>
        pathEndOrSingleSlash {
          get {
            complete(getReservedSeatCount(imdbId, screenId).map(_.asJson))
          }
        }
      }
  }

}