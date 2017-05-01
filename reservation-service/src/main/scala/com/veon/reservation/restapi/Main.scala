package com.veon.reservation.restapi

import java.util.UUID

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.google.common.net.HostAndPort
import com.orbitz.consul.Consul
import com.veon.common.utils.{DatabaseService, Utils}
import com.veon.consul.discovery.ConsulServiceDiscovery
import com.veon.consul.serviceregistry.{ConsulService, ConsulServiceRegistry}
import com.veon.reservation.restapi.http.HttpService
import com.veon.reservation.restapi.service.ReserveService
import com.veon.reservation.restapi.utils.Config

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object Main extends App with Config {
  implicit val actorSystem = ActorSystem()
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  implicit val log: LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val consul = Consul.builder().withHostAndPort(HostAndPort.fromParts(consulHost, 8500)).build()

  val serviceDiscovery = ConsulServiceDiscovery(consul)

  val databaseService = new DatabaseService(jdbcUrl, dbUser, dbPassword)

  val reserveService = new ReserveService(databaseService, serviceDiscovery)

  val httpService = new HttpService(reserveService)

  import com.veon.common.exception.CustomExceptionHandler.Implicits._

  Http().bindAndHandle(httpService.routes, httpHost, httpPort) andThen {
    case Success(_) =>

      val serviceRegistry = ConsulServiceRegistry(consul.agentClient())

      val service = ConsulService(UUID.randomUUID().toString, serviceName, Utils.ipAddress, httpPort, "dev")

      serviceRegistry.register(service)

      sys.ShutdownHookThread {
        serviceRegistry.deregister(service)
      }
    case Failure(e) => log.info(s"Error starting HTTP server: $e")
  }
}

