package model.car

import model.road.Environment
import model.simulation.SimCommand.Action.Action
import model.simulation.SimCommand.Percept.Percept

trait Agent:
  val id: String
  
  def env: Environment

  def env_=(env: Environment): Unit

  def timeDt: Int

  def timeDt_=(dt: Int): Unit

  def init(newEnv: Environment): Unit = env = newEnv 

  def step(dt: Int): Unit

  def currentPercepts: Percept = env.currentPercepts(id)

  def doAction(action: Action): Unit = env.doAction(id, action)



    
    