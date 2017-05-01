package com.veon.consul.discovery

import com.veon.restclient.servicefactory.ServiceFactory
import io.atomicbits.scraml.dsl.{PlainSegment, Response}

import scala.concurrent.Future

case class ServiceNotFoundException(message: String) extends RuntimeException(message)

class ServiceFinder(serviceDiscovery: ServiceDiscovery) {

  def withService[T <: PlainSegment, R](name: String, serviceFactory: ServiceFactory[T])
                                       (f: T => Future[Response[R]]): Future[Response[R]] = {
    serviceDiscovery.findService(name) match {
      case Some(service) => f(serviceFactory.getService(service))
      case None => Future.failed(ServiceNotFoundException(s"Service $name has not been found!"))
    }
  }
}

object ServiceFinder {
  def apply(serviceDiscovery: ServiceDiscovery): ServiceFinder = new ServiceFinder(serviceDiscovery)
}