package com.veon.aggregation.restapi.service

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import com.veon.common.exception.InternalException
import com.veon.consul.discovery.{ServiceDiscovery, ServiceFinder}
import com.veon.restclient.servicefactory.{MoviesServiceFactory, ReservationServiceFactory}

import scala.concurrent.{ExecutionContext, Future}

class AggregationService(serviceDiscovery: ServiceDiscovery)
                        (implicit executionContext: ExecutionContext, actorSystem: ActorSystem) {

  implicit val log: LoggingAdapter = Logging(actorSystem, getClass)

  case class MovieInfo(imdbId: String, screenId: String, movieTitle: String, availableSeats: Long, reservedSeats: Long)

  def retrieveMovieInfo(imdbId: String, screenId: String): Future[MovieInfo] = {
    ServiceFinder(serviceDiscovery).withService("Movies", MoviesServiceFactory()) {
      service =>
        service.imdbId(imdbId).screenId(screenId).get()
    }.flatMap {
      response => {
        log.info(response.stringBody.get)

        response.body match {
          case Some(movie) =>
            ServiceFinder(serviceDiscovery).withService("Reservation", ReservationServiceFactory()) {
              service =>
                service.imdbId(imdbId).screenId(screenId).get()
            }.flatMap {
              reservedSeats =>
                reservedSeats.body match {
                  case Some(seats) => Future.successful(MovieInfo(movie.imdbId, movie.screenId, movie.title, movie.availableSeats, seats))
                  case None => Future.failed[MovieInfo](InternalException("Movie has not been found!"))
                }
            }

          case None => Future.failed[MovieInfo](InternalException("Movie has not been found!"))
        }
      }
    }
  }
}
