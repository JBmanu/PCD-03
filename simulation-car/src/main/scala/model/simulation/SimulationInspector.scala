package model.simulation


import model.car.Agent
import model.inspector.{ RoadSimStatistics, Stepper, TimeStatistics }
import model.road.Environment

trait SimulationInspector:

  def stepper(): Stepper

  def timeStatistics(): TimeStatistics

  def roadSimStatistics(): RoadSimStatistics
  

  def environment(): Environment

  def agents(): List[Agent]

  def setup(): Unit
  
  
    
    