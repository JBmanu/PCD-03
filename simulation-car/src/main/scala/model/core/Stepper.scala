package model.core

trait Stepper:
  val totalStep: Int

  val currentStep: Int

  val hasMoreSteps: Boolean

  def setTotalStep(value: Int): Stepper

  def nextStep(): Stepper


object Stepper:

  def apply(initTotalStep: Int): Stepper = StepperImpl(initTotalStep, 0)

  def zero(): Stepper = StepperImpl(0, 0)

  private case class StepperImpl(totalStep: Int, currentStep: Int) extends Stepper:
    override val hasMoreSteps: Boolean = currentStep < totalStep

    override def setTotalStep(value: Int): Stepper = copy(totalStep = value)

    override def nextStep(): Stepper = copy(currentStep = currentStep + 1)

