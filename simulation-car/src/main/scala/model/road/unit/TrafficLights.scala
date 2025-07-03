package model.road.unit

import model.core.P2ds.P2d

object TrafficLights:

  enum TrafficLightState:
    case GREEN, YELLOW, RED


  trait TrafficLight:

    def init: TrafficLight

    def step(dt: Int): TrafficLight

    def position: P2d

    def isGreen: Boolean

    def isYellow: Boolean

    def isRed: Boolean


  object TrafficLight:

    def apply(position: P2d, initialState: TrafficLightState,
              redDuration: Int, greenDuration: Int, yellowDuration: Int): TrafficLight = {
      val initTimeState = 0
      TrafficLightImpl(position, initialState, initTimeState, initialState, redDuration, greenDuration, yellowDuration)
    }

    private case class TrafficLightImpl(position: P2d, state: TrafficLightState, currentTimeInState: Int,
                                        initialState: TrafficLightState,
                                        redDuration: Int, greenDuration: Int, yellowDuration: Int) extends TrafficLight:

      private def changeState(newState: TrafficLightState): TrafficLight =
        this.copy(state = newState, currentTimeInState = 0)

      override def init: TrafficLight = this.copy(state = initialState, currentTimeInState = 0)

      override def step(dt: Int): TrafficLight = this.state match
        case TrafficLightState.GREEN  =>
          val newCurrentTimeInState = this.currentTimeInState + dt
          if (newCurrentTimeInState >= greenDuration) this.changeState(TrafficLightState.YELLOW)
          else this.copy(currentTimeInState = newCurrentTimeInState)
        case TrafficLightState.RED    =>
          val newCurrentTimeInState = this.currentTimeInState + dt
          if (newCurrentTimeInState >= redDuration) this.changeState(TrafficLightState.GREEN)
          else this.copy(currentTimeInState = newCurrentTimeInState)
        case TrafficLightState.YELLOW =>
          val newCurrentTimeInState = this.currentTimeInState + dt
          if (newCurrentTimeInState >= yellowDuration) this.changeState(TrafficLightState.RED)
          else this.copy(currentTimeInState = newCurrentTimeInState)

      override def isGreen: Boolean = state equals TrafficLightState.GREEN

      override def isYellow: Boolean = state equals TrafficLightState.YELLOW

      override def isRed: Boolean = state equals TrafficLightState.RED




