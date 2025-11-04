package example

import akka.actor.typed.ActorSystem
import model.actors.ActorSimulation.Start
import model.actors.{ ActorCar, ActorSimulation }
import model.car.CarAgentBasic
import model.core.P2ds.P2d
import model.core.{ Scheduler, Stepper }
import model.road.RoadEnv
import model.simulation.Simulation

object RunTrafficSimulationMassiveTest:

  def main(args: Array[String]): Unit =
    val scheduler = Scheduler(10, 0, 1)
    val stepper = Stepper(100)

    val env = RoadEnv()
    val road = env.createRoad(P2d(0, 300), P2d(15000, 300))


    val cars = (1 to 500).map(i =>
                                val carId = "car-" + i
                                val initialPos = i * 10
                                val carAcceleration = 1
                                val carDeceleration = 0.3
                                val carMaxSpeed = 7
                                CarAgentBasic(carId, env, road, initialPos, carAcceleration, carDeceleration, carMaxSpeed))
    val actorCars = cars.map(car => ActorSystem(ActorCar(car), car.id)).toList

    val simulation = Simulation()
    simulation.setup(scheduler, stepper, env, actorCars)

    val actorSimulation = ActorSystem(ActorSimulation(simulation), "Simulation")
    actorSimulation ! Start

