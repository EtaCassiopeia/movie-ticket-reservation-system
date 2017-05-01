package com.veon.consul.serviceregistry

trait DiscoverableService {
  def name:String
  def id:String
}

abstract class ServiceRegistry[T <: DiscoverableService] {
  def register(service: T): Unit

  def deregister(serviceRegistry: T): Unit
}