package com.veon.consul.discovery

import com.orbitz.consul.Consul
import com.orbitz.consul.model.health.Service

import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.util.Random


class ConsulServiceDiscovery(consul: Consul) extends ServiceDiscovery {
  private lazy val healthClient = consul.healthClient()
  private val roundRobinIndexFor = mutable.Map.empty[String, Int]

  override def findService(serviceName: String, pickStrategy: PickStrategy = PickRoundRobinInstance): Option[Service] = {
    val serviceHealth = healthClient.getHealthyServiceInstances(serviceName).getResponse

    val services = serviceHealth.toList.map(s => s.getService)

    pickStrategy match {
      case PickFirstInstance => pickFirstInstance(services)
      case PickRandomInstance => pickRandomInstance(services)
      case PickRoundRobinInstance => pickRoundRobinInstance(serviceName, services)
    }
  }

  private def pickFirstInstance(services: List[Service]): Option[Service] = {
    services.headOption
  }

  private def pickRandomInstance(services: List[Service]): Option[Service] = {
    if (services.nonEmpty)
      Some(services(Random.nextInt(services.size - 1)))
    else
      None
  }

  private def pickRoundRobinInstance(name: String, services: List[Service]): Option[Service] = {
    if (services.nonEmpty) {
      roundRobinIndexFor.putIfAbsent(name, 0)

      val currentIndex = roundRobinIndexFor(name)
      val nextIndex =
        if (services.size > currentIndex + 1) currentIndex + 1
        else 0
      roundRobinIndexFor += (name -> nextIndex)

      Some(services(currentIndex))
    }
    else
      None
  }

}

object ConsulServiceDiscovery {
  def apply(consul: Consul): ConsulServiceDiscovery = new ConsulServiceDiscovery(consul)
}
