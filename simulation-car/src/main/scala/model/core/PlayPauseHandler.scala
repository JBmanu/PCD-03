package model.core

trait PlayPauseHandler:
  val isInPause: Boolean

  def play(): PlayPauseHandler

  def pause(): PlayPauseHandler

object PlayPauseHandler:

  def apply(): PlayPauseHandler = PlayPauseHandlerImpl(false)

  private case class PlayPauseHandlerImpl(isInPause: Boolean) extends PlayPauseHandler:

    override def play(): PlayPauseHandler = copy(false)

    override def pause(): PlayPauseHandler = copy(true)
