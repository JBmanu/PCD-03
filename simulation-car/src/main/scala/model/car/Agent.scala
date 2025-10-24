package model.car

import model.road.Environment
import model.simulation.SimCommand.Action.Action
import model.simulation.SimCommand.Percept.Percept

abstract class Agent(val id: String):
  private var _env: Environment = _

  def env: Environment = _env

  private def env_=(env: Environment): Unit = _env = env

  def timeDt: Int

  def timeDt_=(dt: Int): Unit

  def init(newEnv: Environment): Unit = env = newEnv

  def step(dt: Int): Unit

  def currentPercepts: Percept = env.currentPercepts(id)

  def doAction(action: Action): Unit = env.doAction(id, action)



    
    