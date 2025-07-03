package model.simulation

object Simulations:

  trait Simulation

  object Simulation:
    def apply(): Simulation = SimulationImpl()

    private case class SimulationImpl() extends Simulation
