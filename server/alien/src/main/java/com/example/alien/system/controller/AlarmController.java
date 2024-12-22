package com.example.alien.system.controller;


import com.example.alien.system.Entity.AlienClock;
import com.example.alien.system.service.AlarmService;
import com.example.alien.system.service.AlienTimeConvertService;
import com.example.alien.system.util.AlarmWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alarm")
public class AlarmController {
    @Autowired
    private AlarmService alarmService;

    @Autowired
    private AlienTimeConvertService alienTimeConvertService;

    @Autowired
    private AlarmWebSocketHandler alarmWebSocketHandler;

    @RequestMapping("/clear/{sessionId}")
    public void clearAlarms(@PathVariable String sessionId) {
        this.alarmService.clearAlarms(sessionId);
    }

    @RequestMapping("/add/{sessionId}")
    public void addAlarm(@PathVariable String sessionId, @RequestBody AlienClock alarm) {
        this.alarmService.addAlarm(sessionId, alarm);
    }

    @RequestMapping("/delete/{sessionId}/{index}")
    public void removeAlarm(@PathVariable String sessionId, @PathVariable String index) {
        this.alarmService.removeByIndex(sessionId, Integer.parseInt(index));
    }

    @RequestMapping("/get/{sessionId}")
    public List<AlienClock> getAlarm(@PathVariable String sessionId) {
        return this.alarmService.getAlarms(sessionId);
    }

    @RequestMapping("/test")
    public void test(@RequestParam String sessionId) {
        this.alarmWebSocketHandler.broadcastAlarm("test");
        System.out.println("test");
    }



}
