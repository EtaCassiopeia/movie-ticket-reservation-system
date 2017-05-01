package com.veon.common.exception

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, NotFound}
import akka.http.scaladsl.server.Directives.{complete, extractUri}
import akka.http.scaladsl.server.ExceptionHandler


object CustomExceptionHandler {
  object Implicits {
    implicit def exceptionHandler: ExceptionHandler =
      ExceptionHandler {
        case e:InternalException =>
          extractUri { uri =>
            complete(HttpResponse(NotFound, entity = e.getMessage))
          }
        case _ =>
          extractUri { uri =>
            complete(HttpResponse(InternalServerError, entity =s"Request to $uri could not be handled normally"))
          }
      }
  }
}
