package model.simulation


import model.car.Agent
import model.inspector.{ RoadSimStatistics, Stepper, TimeStatistics }
import model.road.Environment

trait SimulationInspector:
  val stepper: Stepper
  val timeStatistics: TimeStatistics
  val roadSimStatistics: RoadSimStatistics


  def environment(): Environment

  def agents(): List[Agent]

  def setup(): Unit



    