package com.veon.common.utils

import java.net.InetAddress

object Utils {
  def ipAddress: String = InetAddress.getLocalHost.getHostAddress
}
