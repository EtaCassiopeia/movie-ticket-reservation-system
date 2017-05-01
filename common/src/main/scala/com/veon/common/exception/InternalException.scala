package com.veon.common.exception

case class InternalException(message: String = "", cause: Throwable = None.orNull)
  extends RuntimeException(message, cause)
