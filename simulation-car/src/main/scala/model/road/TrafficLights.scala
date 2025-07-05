package model.road

import model.core.P2ds.P2d

object TrafficLights:

  enum TrafficLightState:
    case GREEN, YELLOW, RED


  trait TrafficLight:

    def init(): Unit

    def step(dt: Int): Unit

    def position: P2d

    def isGreen: Boolean

    def isYellow: Boolean

    def isRed: Boolean


  object TrafficLight:

    def apply(position: P2d, initialState: TrafficLightState,
              redDuration: Int, greenDuration: Int, yellowDuration: Int): TrafficLight =
      val initTimeState = 0
      TrafficLightImpl(initialState, initTimeState, position, initialState, redDuration, greenDuration, yellowDuration)


    private case class TrafficLightImpl(private var _state: TrafficLightState, var _currentTimeInState: Int,
                                        position: P2d, initialState: TrafficLightState,
                                        redDuration: Int, greenDuration: Int, yellowDuration: Int) extends TrafficLight:

      private def changeState(newState: TrafficLightState): Unit =
        _state = newState
        _currentTimeInState = 0

      override def init(): Unit = changeState(initialState)

      override def step(dt: Int): Unit =
        _currentTimeInState += dt
        _state match
          case TrafficLightState.GREEN  => if (_currentTimeInState >= greenDuration) changeState(TrafficLightState.YELLOW)
          case TrafficLightState.RED    => if (_currentTimeInState >= redDuration) changeState(TrafficLightState.GREEN)
          case TrafficLightState.YELLOW => if (_currentTimeInState >= yellowDuration) changeState(TrafficLightState.RED)

      override def isGreen: Boolean = _state equals TrafficLightState.GREEN

      override def isYellow: Boolean = _state equals TrafficLightState.YELLOW

      override def isRed: Boolean = _state equals TrafficLightState.RED




