package model.road

import model.simulation.Command.Action.Action
import model.simulation.Command.Percept.Percept

trait Environment:
  val id: String
  
  def init(): Environment

  def step(dt: Int): Environment

  def currentPercepts(agentId: String): Percept

  def doAction(agentId: String, action: Action): Environment

