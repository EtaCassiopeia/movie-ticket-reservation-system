package com.veon.consul.serviceregistry

import akka.actor.{Actor, ActorSystem, Props}
import com.orbitz.consul.AgentClient
import com.orbitz.consul.model.agent.ImmutableRegistration
import com.orbitz.consul.model.agent.Registration.RegCheck

case class ConsulService(id: String, name: String, host: String, port: Int, tags: String*)
  extends DiscoverableService

class ConsulServiceRegistry(agentClient: AgentClient)(implicit actorSystem: ActorSystem)
  extends ServiceRegistry[ConsulService] {

  override def register(service: ConsulService): Unit = {

    val registration = ImmutableRegistration
      .builder()
      .id(service.id)
      .name(service.name)
      .address(service.host)
      .port(service.port)
      .addChecks(RegCheck.ttl(3))
      .build()

    agentClient.register(registration)

    actorSystem.actorOf(Props(new HealthCheckActor(agentClient, service.id)), s"HealthCheckActor-${service.name}-${service.id}")
  }

  override def deregister(service: ConsulService): Unit =
    agentClient.deregister(service.id)


  object Messages {

    case object Tick

  }

  class HealthCheckActor(agentClient: AgentClient, serviceId: String) extends Actor {

    import Messages._

    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._

    override def preStart(): Unit =
      context.system.scheduler.schedule(1000 millis, 2000 millis, self, Tick)

    override def receive: Receive = {
      case Tick => agentClient.pass(serviceId)
    }
  }

}

object ConsulServiceRegistry {
  def apply(agentClient: AgentClient)(implicit actorSystem: ActorSystem): ConsulServiceRegistry =
    new ConsulServiceRegistry(agentClient)(actorSystem)
}
