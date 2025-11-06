package wrapper.core

trait Stepper:
  val totalStep: Int

  val currentStep: Int

  val hasMoreSteps: Boolean

  def setTotalSteps(value: Int): Stepper

  def nextStep(): Stepper


object Stepper:

  def zero(): Stepper = Stepper(0)

  def apply(totalStep: Int): Stepper = StepperImpl(totalStep, 0)


  private case class StepperImpl(totalStep: Int, currentStep: Int) extends Stepper:
    override val hasMoreSteps: Boolean = currentStep < totalStep

    override def setTotalSteps(value: Int): Stepper = copy(totalStep = value)

    override def nextStep(): Stepper = copy(currentStep = currentStep + 1)

