package model.car

import model.road.Environment
import model.simulation.Command.Action.Action
import model.simulation.Command.Percept.Percept

trait Agent:
  val id: String
  val env: Environment

  def init(env: Environment): Agent

  def step(dt: Int): Agent

  def currentPercepts: Percept

  def doAction(act: Action): Agent

  def setTimeDt(dt: Int): Agent
  
    
    