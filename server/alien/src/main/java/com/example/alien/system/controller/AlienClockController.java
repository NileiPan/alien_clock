package com.example.alien.system.controller;

import com.example.alien.system.Entity.AlienClock;
import com.example.alien.system.service.AlienTimeConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController

@RequestMapping("/api/alien-clock")
public class AlienClockController {

    @Autowired
    private AlienTimeConvertService alienTimeConvertService;

    /**
     * @return: Map<String, Integer>
     */
    @GetMapping("/current-time")
    public Map<String, Long> getCurrentTime(){
        return this.alienTimeConvertService.getCurrentAlienTime().getAlienTime();
    }

    /**
     * @param: AlienClock
     */
    @PostMapping("/set-time")
    public LocalDateTime setCurrentTime(@RequestBody AlienClock alienTime){
        this.alienTimeConvertService.setCurrentAlienTime(alienTime);
        return this.alienTimeConvertService.alienToEarth(alienTime);
    }

    /**
     * @return: LocalDateTime
     */
    @GetMapping("/earth-time")
    public LocalDateTime getEarthTime(){
        return this.alienTimeConvertService.alienToEarth(this.alienTimeConvertService.getCurrentAlienTime());
    }

}
