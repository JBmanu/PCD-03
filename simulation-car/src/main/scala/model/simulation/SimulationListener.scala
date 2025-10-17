package model.simulation


trait SimulationListener:
  
  def notifyInit(t: Int, simulation: SimulationInspector): Unit
  
  def notifyStepDone(t: Int, simulation: SimulationInspector): Unit
  
  def notifyEnd(simulation: SimulationInspector): Unit 
