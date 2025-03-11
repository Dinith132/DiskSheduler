package org.example.disksheduler;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;
//import java.util.PriorityQueue;

public class DiskScheduler {

    public DiskScheduleResult FCFS(int headPosition, int[] requests) {
        int totalSeekTime = 0;
        int currentPosition = headPosition;
        List<Integer> seekSequence = new ArrayList<>();
        seekSequence.add(headPosition);

        for (int request : requests) {
            totalSeekTime += Math.abs(request - currentPosition);
            currentPosition = request;
            seekSequence.add(currentPosition);
        }

        return new DiskScheduleResult(totalSeekTime, seekSequence);
    }

    public DiskScheduleResult SSTF(int headPosition, int[] requests) {
        List<Integer> requestsList = new ArrayList<>();
        for (int req : requests) {
            requestsList.add(req);
        }

        int totalSeekTime = 0;
        int currentPosition = headPosition;
        List<Integer> seekSequence = new ArrayList<>();
        seekSequence.add(headPosition);

        while (!requestsList.isEmpty()) {
            int nextRequest = findClosestRequest(currentPosition, requestsList);
            totalSeekTime += Math.abs(nextRequest - currentPosition);
            currentPosition = nextRequest;
            seekSequence.add(currentPosition);
            requestsList.remove(Integer.valueOf(nextRequest));
        }

        return new DiskScheduleResult(totalSeekTime, seekSequence);
    }

    private int findClosestRequest(int position, List<Integer> requests) {
        int minDistance = Integer.MAX_VALUE;
        int closestRequest = -1;

        for (int request : requests) {
            int distance = Math.abs(position - request);
            if (distance < minDistance) {
                minDistance = distance;
                closestRequest = request;
            }
        }

        return closestRequest;
    }

    public DiskScheduleResult SCAN(int headPosition, int[] requests) {
        int totalSeekTime = 0;
        int currentPosition = headPosition;
        List<Integer> seekSequence = new ArrayList<>();
        seekSequence.add(headPosition);

        List<Integer> requestsList = new ArrayList<>();
        for (int req : requests) {
            requestsList.add(req);
        }

        int end = 199;
        int start = 0;

        // Move towards higher track numbers
        for (int i = currentPosition; i <= end; i++) {
            if (requestsList.contains(i) || i == end) {
                totalSeekTime += Math.abs(currentPosition - i);
                currentPosition = i;
                seekSequence.add(i);
                requestsList.remove(Integer.valueOf(i));
            }
        }

        totalSeekTime += Math.abs(currentPosition - end);
        currentPosition = end;

        // Move towards lower track numbers
        for (int i = end - 1; i >= start; i--) {
            if (requestsList.contains(i)) {
                totalSeekTime += Math.abs(currentPosition - i);
                currentPosition = i;
                seekSequence.add(i);
                requestsList.remove(Integer.valueOf(i));
            }
        }

        return new DiskScheduleResult(totalSeekTime, seekSequence);
    }

    public DiskScheduleResult C_SCAN(int headPosition, int[] requests) {
        int totalSeekTime = 0;
        int currentPosition = headPosition;
        List<Integer> seekSequence = new ArrayList<>();
        seekSequence.add(headPosition);

        List<Integer> requestsList = new ArrayList<>();
        for (int req : requests) {
            requestsList.add(req);
        }

        int end = 199;
        int start = 0;

        // Move towards higher track numbers
        for (int i = currentPosition; i <= end; i++) {
            if (requestsList.contains(i) || i == end) {
                totalSeekTime += Math.abs(currentPosition - i);
                currentPosition = i;
                seekSequence.add(i);
                requestsList.remove(Integer.valueOf(i));
            }
        }

        totalSeekTime += Math.abs(currentPosition - end);
        currentPosition = end;

        // Jump to start
        totalSeekTime += Math.abs(currentPosition - start);
        currentPosition = start;

        // Move towards head position
        for (int i = start; i <= headPosition; i++) {
            if (requestsList.contains(i) || i == start) {
                totalSeekTime += Math.abs(currentPosition - i);
                currentPosition = i;
                seekSequence.add(i);
                requestsList.remove(Integer.valueOf(i));
            }
        }

        return new DiskScheduleResult(totalSeekTime, seekSequence);
    }

    public DiskScheduleResult C_LOOK(int headPosition, int[] requests) {
        int totalSeekTime = 0;
        int currentPosition = headPosition;
        List<Integer> seekSequence = new ArrayList<>();
        seekSequence.add(headPosition);

        List<Integer> requestsList = new ArrayList<>();
        for (int req : requests) {
            requestsList.add(req);
        }

        int max = findMax(requests);
        int min = findMin(requests);

        // Move towards higher track numbers
        for (int i = currentPosition; i <= max; i++) {
            if (requestsList.contains(i)) {
                totalSeekTime += Math.abs(currentPosition - i);
                currentPosition = i;
                seekSequence.add(i);
                requestsList.remove(Integer.valueOf(i));
            }
        }

        // Jump to min
        totalSeekTime += Math.abs(currentPosition - min);
        currentPosition = min;

        // Move towards head position
        for (int i = min; i <= headPosition; i++) {
            if (requestsList.contains(i)) {
                totalSeekTime += Math.abs(currentPosition - i);
                currentPosition = i;
                seekSequence.add(i);
                requestsList.remove(Integer.valueOf(i));
            }
        }

        return new DiskScheduleResult(totalSeekTime, seekSequence);
    }

    private int findMax(int[] array) {
        int max = Integer.MIN_VALUE;
        for (int value : array) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int findMin(int[] array) {
        int min = Integer.MAX_VALUE;
        for (int value : array) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }
}

