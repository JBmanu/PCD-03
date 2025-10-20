package model.core

trait Stepper:
  val totalStep: Int

  val currentStep: Int

  def setTotalStep(value: Int): Stepper

  def nextStep(): Stepper

  def hasMoreSteps: Boolean


object Stepper:

  def apply(initTotalStep: Int): Stepper = StepperImpl(initTotalStep, 0)

  def zero(): Stepper = StepperImpl(0, 0)

  private case class StepperImpl(totalStep: Int, currentStep: Int) extends Stepper:

    override def setTotalStep(value: Int): Stepper = copy(totalStep = totalStep)

    override def nextStep(): Stepper = copy(currentStep = currentStep + 1)

    override def hasMoreSteps: Boolean = currentStep < totalStep

