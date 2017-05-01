package com.veon.consul.discovery

import com.orbitz.consul.model.health.Service

sealed trait PickStrategy

case object PickFirstInstance extends PickStrategy

case object PickRandomInstance extends PickStrategy

case object PickRoundRobinInstance extends PickStrategy

abstract class ServiceDiscovery {
  def findService(name: String,pickStrategy: PickStrategy=PickRoundRobinInstance): Option[Service]
}
