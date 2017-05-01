package com.veon.restclient.servicefactory

import com.orbitz.consul.model.health.Service
import com.veon.restclient.api.reserves.ReservesResource

class ReservationServiceFactory
  extends ServiceFactory[ReservesResource] {
  override def getService(service: Service): ReservesResource = restClient(service).api.reserves
}


object ReservationServiceFactory{
  def apply(): ReservationServiceFactory = new ReservationServiceFactory()
}