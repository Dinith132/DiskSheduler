package org.example.disksheduler;

import java.util.List;

public class DiskScheduleResult {
    final private int totalSeekTime;
    final private int[] seekSequence;

    public DiskScheduleResult(int totalSeekTime, List<Integer> seekSequence) {
        this.totalSeekTime = totalSeekTime;
        this.seekSequence = new int[seekSequence.size()];

        for (int i = 0; i < seekSequence.size(); i++) {
            this.seekSequence[i] = seekSequence.get(i);
        }
    }

    public int getTotalSeekTime() {
        return totalSeekTime;
    }

    public int[] getSeekSequence() {
        return seekSequence;
    }
}
