package model.simulation

import model.car.Agent
import model.inspector.{ RoadSimStatistics, Stepper }

object Simulations:

  trait Simulation

  object Simulation:
    def apply(): Simulation = SimulationImpl()

    private case class SimulationImpl() extends Simulation:

      //      private var _environment: Environment = Nil
      private var _agents: List[Agent] = List.empty

      // variables of control and manage simulation
      private var toBeInSyncWithWallTime: Boolean = false
      private var nStepsPerSec: Int = 0
      private var dt: Int = 0
      private var t0: Int = 0

      private val roadStatistics: RoadSimStatistics = RoadSimStatistics()
      private val stepper: Stepper = Stepper()

// simulation listeners
// per il model
// per la view

// model
//      private var roadStatistics
//      private var timeStatistics
//      private var stepper

