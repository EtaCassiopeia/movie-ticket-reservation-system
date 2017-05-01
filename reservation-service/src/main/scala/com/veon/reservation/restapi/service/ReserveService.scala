package com.veon.reservation.restapi.service

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import com.veon.common.exception.InternalException
import com.veon.common.utils.DatabaseService
import com.veon.consul.discovery.{ServiceDiscovery, ServiceFinder}
import com.veon.reservation.restapi.model.ReserveEntity
import com.veon.reservation.restapi.model.db.ReserveEntityTable
import com.veon.restclient.servicefactory.MoviesServiceFactory
import slick.jdbc.meta.MTable

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class ReserveService(val databaseService: DatabaseService, serviceDiscovery: ServiceDiscovery)
                    (implicit executionContext: ExecutionContext, actorSystem: ActorSystem) extends ReserveEntityTable {

  import databaseService._
  import databaseService.driver.api._

  private val tables = List(reserves)

  private val existing = db.run(MTable.getTables)
  private val f = existing.flatMap(v => {
    val names = v.map(mt => mt.name.name)
    val createIfNotExist = tables.filter(table =>
      !names.contains(table.baseTableRow.tableName)).map(_.schema.create)
    db.run(DBIO.sequence(createIfNotExist))
  })
  Await.result(f, Duration.Inf)

  implicit val log: LoggingAdapter = Logging(actorSystem, getClass)

  def getReservationById(id: Long): Future[Option[ReserveEntity]] = db.run(reserves.filter(_.id === id).result.headOption)

  def getReservedSeatCount(imdbId: String, screenId: String): Future[Int] = {
    db.run(reserves.filter(r => r.imdbId === imdbId && r.screenId === screenId).length.result)
  }

  def reserveSeat(reserve: ReserveEntity): Future[ReserveEntity] = {
    ServiceFinder(serviceDiscovery).withService("Movies", MoviesServiceFactory()) {
      service =>
        service.imdbId(reserve.imdbId).screenId(reserve.screenId).get()
    }.flatMap {
      response => {
        log.info(response.stringBody.get)

        response.body match {
          case Some(movie) =>
            val reservedSeatsFuture = getReservedSeatCount(reserve.imdbId, reserve.screenId)

            reservedSeatsFuture.flatMap { i =>

              if (i < movie.availableSeats)
                db.run(reserves returning reserves += reserve)
              else
                Future.failed[ReserveEntity](InternalException("All seats have been allocated!"))
            }

          case None => Future.failed[ReserveEntity](InternalException("Movie has not been found!"))
        }

      }
    }
  }

  case class ReservationInfo(imdbId: String, screenId: String, reservedSeats: Int)

}