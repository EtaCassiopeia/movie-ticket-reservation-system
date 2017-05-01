package com.veon.restclient.servicefactory

import java.nio.charset.Charset

import com.orbitz.consul.model.health.Service
import com.veon.restclient.VeonApi
import io.atomicbits.scraml.dsl.PlainSegment
import io.atomicbits.scraml.dsl.client.ClientConfig

abstract class ServiceFactory[T <: PlainSegment] {
  protected def restClient(service: Service) = VeonApi(
    host = service.getAddress,
    port = service.getPort,
    protocol = "http",
    defaultHeaders = Map("Accept" -> "application/json"),
    prefix = None,
    config = ClientConfig(requestCharset = Charset.forName("UTF-8")),
    clientFactory = None
  )

  def getService(service: Service): T
}