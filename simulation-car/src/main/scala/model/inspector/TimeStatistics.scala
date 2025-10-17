package model.inspector

trait TimeStatistics

object TimeStatistics:

  def apply(): TimeStatistics = TimeStatisticsImpl()

  private case class TimeStatisticsImpl() extends TimeStatistics
