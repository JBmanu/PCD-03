package model.inspector

trait Stepper

object Stepper:
  
  def apply(): Stepper = StepperImpl()

  private case class StepperImpl() extends Stepper
