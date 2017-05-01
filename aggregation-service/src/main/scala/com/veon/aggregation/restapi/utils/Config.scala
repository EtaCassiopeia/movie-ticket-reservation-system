package com.veon.aggregation.restapi.utils

import com.typesafe.config.ConfigFactory

trait Config {
  private val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")
  private val serviceConfig = config.getConfig("service")
  private val consulConfig = config.getConfig("consul")

  val httpHost = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")

  val serviceName = serviceConfig.getString("name")

  val consulHost = consulConfig.getString("host")
}
