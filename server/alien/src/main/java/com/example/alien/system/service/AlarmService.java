package com.example.alien.system.service;

import com.example.alien.system.Entity.AlienClock;
import com.example.alien.system.util.AlarmWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class AlarmService {
    private final Map<String, ArrayList<AlienClock>> alarms = new HashMap<>();

    @Autowired
    private AlarmWebSocketHandler alarmWebSocketHandler;

    @Autowired
    private AlienTimeConvertService alienTimeConvertService;

    public void addAlarm(String sessionId, AlienClock alarm) {
        if (!this.alarms.containsKey(sessionId)) {
            this.alarms.put(sessionId, new ArrayList<>());
        }
        this.alarms.get(sessionId).add(alarm);
    }

    public void removeAlarm(String sessionId, int index) {
        this.alarms.get(sessionId).remove(index);
    }

    public void removeByIndex(String sessionId, int index) {
        this.alarms.get(sessionId).remove(index);
    }
    public List<AlienClock> getAlarms(String sessionId) {
        return this.alarms.get(sessionId);
    }

    public void clearAlarms(String sessionId) {
        this.alarms.get(sessionId).clear();
    }

    @Scheduled(fixedRate = 500)
    public void checkAlarms() {
        AlienClock currentTime = this.alienTimeConvertService.getCurrentAlienTime();
        for (String sessionId : this.alarms.keySet()) {
            for (AlienClock alarm : this.alarms.get(sessionId)) {
                if (alarm.equals(currentTime)) {
                    this.alarmWebSocketHandler.broadcastAlarm("alarm:" + alarm);
                }
            }
        }
    }

}
