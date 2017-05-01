package com.veon.aggregation.restapi.http.routes

import akka.http.scaladsl.server.Directives._
import com.veon.aggregation.restapi.service.AggregationService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext

class AggregationServiceRoute(aggregationService: AggregationService)
                             (implicit executionContext: ExecutionContext) extends FailFastCirceSupport {

  import aggregationService._

  val route = pathPrefix("info") {
    pathPrefix(Segment / Segment) { (imdbId, screenId) =>
      pathEndOrSingleSlash {
        get {
          complete(retrieveMovieInfo(imdbId, screenId).map(_.asJson))
        }
      }
    }
  }

}
