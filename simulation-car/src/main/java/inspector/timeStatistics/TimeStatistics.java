package inspector.timeStatistics;

public class TimeStatistics {
    private long currentWallTime;
    private long startWallTime;
    private long endWallTime;
    private double averageTimeForStep;

    public TimeStatistics() {
        this.currentWallTime = 0;
        this.startWallTime = 0;
        this.endWallTime = 0;
        this.averageTimeForStep = 0.d;
    }

    public long currentWallTime() {
        return this.currentWallTime;
    }

    public long currentWallTimeSubtractStartWallTime() {
        return this.currentWallTime - this.startWallTime;
    }

    public double averageTimeForStep() {
        return this.averageTimeForStep;
    }

    public long totalWallTime() {
        return this.endWallTime - this.startWallTime;
    }


    public void setCurrentWallTime(final long currentWallTime) {
        this.currentWallTime = currentWallTime;
    }

    public void setStartWallTime() {
        this.startWallTime = System.currentTimeMillis();
        this.endWallTime = this.startWallTime;
    }

    public void setEndWallTime(final long endWallTime) {
        this.endWallTime = endWallTime;
    }

    public void setAverageTimeForStep(final double averageTimeForStep) {
        this.averageTimeForStep = averageTimeForStep;
    }
}
