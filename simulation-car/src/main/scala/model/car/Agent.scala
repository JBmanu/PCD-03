package model.car

import model.road.Environment
import model.simulation.SimCommand.Action.Action
import model.simulation.SimCommand.Percept.Percept

trait Agent:
  val id: String

  def env: Environment

  def timeDt: Int

  def timeDt_=(dt: Int): Unit

  def currentPercepts: Percept

  def init(env: Environment): Unit

  def step(dt: Int): Unit

  def doAction(act: Action): Unit



    
    