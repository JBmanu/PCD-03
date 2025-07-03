package model.road.unit

trait Road

object Road:
  
  def apply(): Road = RoadImpl()

  private case class RoadImpl() extends Road
