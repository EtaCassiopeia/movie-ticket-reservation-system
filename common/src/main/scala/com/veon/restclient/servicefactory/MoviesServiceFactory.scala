package com.veon.restclient.servicefactory

import com.orbitz.consul.model.health.Service
import com.veon.restclient.api.movies.MoviesResource

class MoviesServiceFactory
  extends ServiceFactory[MoviesResource] {

  override def getService(service: Service): MoviesResource = restClient(service).api.movies
}

object MoviesServiceFactory{
  def apply(): MoviesServiceFactory = new MoviesServiceFactory()
}
