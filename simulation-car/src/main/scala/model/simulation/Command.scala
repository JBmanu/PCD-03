package model.simulation

object Command:
  
  object Action:
    trait Action

    case class MoveForward(distance: Double) extends Action
    
    
  object Percept:
    trait Percept
    
    