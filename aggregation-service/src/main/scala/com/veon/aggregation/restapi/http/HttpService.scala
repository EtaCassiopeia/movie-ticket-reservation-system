package com.veon.aggregation.restapi.http

import akka.http.scaladsl.server.Directives._
import com.veon.aggregation.restapi.http.routes.AggregationServiceRoute
import com.veon.aggregation.restapi.service.AggregationService
import com.veon.common.utils.CorsSupport

import scala.concurrent.ExecutionContext

class HttpService(aggregationService: AggregationService
                 )(implicit executionContext: ExecutionContext) extends CorsSupport {

  val aggregationRouter = new AggregationServiceRoute(aggregationService)

  val routes =
    pathPrefix("api") {
      corsHandler {
        aggregationRouter.route
      }
    }
}
