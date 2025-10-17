package model.inspector

trait Stepper:
  def totalStep: Int

  def currentStep: Int

  def totalStep_=(totalStep: Int): Unit

  def increaseStep(): Unit

  def hasMoreSteps(): Boolean


object Stepper:

  def apply(): Stepper = StepperImpl()

  private case class StepperImpl() extends Stepper:
    private var _totalStep: Int = 0
    private var _currentSteps: Int = 0

    override def totalStep: Int = _totalStep

    override def currentStep: Int = _currentSteps

    override def totalStep_=(totalStep: Int): Unit = _totalStep = totalStep

    override def increaseStep(): Unit = _currentSteps += 1

    override def hasMoreSteps(): Boolean = currentStep < totalStep
