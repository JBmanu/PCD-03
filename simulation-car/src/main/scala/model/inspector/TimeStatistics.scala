package model.inspector

trait TimeStatistics:

  def currentWallTime: Long
  def startWallTime: Long
  def endWallTime: Long
  def timeElapsedSinceStart(): Long
  def allTimeSpent(): Long

  def averageTimeForStep: Double
  def averageTimeForStep_=(value: Double): Unit

  def updateCurrentTimeWithSystem(): Unit
  def updateStartTimeWithSystem(): Unit
  def updateEndTimeWithSystem(): Unit


object TimeStatistics:

  def apply(): TimeStatistics = TimeStatisticsImpl()

  private case class TimeStatisticsImpl() extends TimeStatistics:
    private var _currentTime = 0L
    private var _startWallTime = 0L
    private var _endWallTime = 0L
    private var _averageTimeForStep = 0d

    override def currentWallTime: Long = _currentTime
    override def startWallTime: Long = _startWallTime
    override def endWallTime: Long = _endWallTime
    override def timeElapsedSinceStart(): Long = currentWallTime - startWallTime
    override def allTimeSpent(): Long = endWallTime - startWallTime

    override def averageTimeForStep: Double = _averageTimeForStep
    override def averageTimeForStep_=(value: Double): Unit = _averageTimeForStep = value

    override def updateCurrentTimeWithSystem(): Unit = _currentTime = System.currentTimeMillis()
    override def updateStartTimeWithSystem(): Unit = _startWallTime = System.currentTimeMillis()
    override def updateEndTimeWithSystem(): Unit = _endWallTime = System.currentTimeMillis()









