package com.veon.reservation.restapi.model.db

import com.veon.common.utils.DatabaseService
import com.veon.reservation.restapi.model.ReserveEntity

trait ReserveEntityTable {

  protected val databaseService: DatabaseService
  import databaseService.driver.api._

  class Reserves(tag: Tag) extends Table[ReserveEntity](tag, "reserves") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def imdbId = column[String]("imdbId")
    def screenId = column[String]("screenId")

    def * = (id, imdbId, screenId) <> ((ReserveEntity.apply _).tupled, ReserveEntity.unapply)
  }

  protected val reserves = TableQuery[Reserves]

}
