package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.stats.Statistics;
import lombok.Data;

@Data
public class StatisticsDTO {
    private float average;
    private int votersCount;
    private int visitorsCount;
    public StatisticsDTO(Statistics statistics){
        average = statistics.getAverage();
        votersCount = statistics.getVotersCount();
        visitorsCount = statistics.getVisitorsCount();
    }
}
