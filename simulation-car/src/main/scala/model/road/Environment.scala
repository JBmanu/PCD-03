package model.road

import model.simulation.SimCommand.Action.Action
import model.simulation.SimCommand.Percept.Percept

trait Environment:
  val id: String
  
  def init(): Unit

  def step(dt: Int): Unit

  def currentPercepts(agentId: String): Percept

  def doAction(agentId: String, action: Action): Unit

