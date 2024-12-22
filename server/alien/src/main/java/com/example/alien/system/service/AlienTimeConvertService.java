package com.example.alien.system.service;

import com.example.alien.system.Entity.AlienClock;
import com.example.alien.system.util.AlarmWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class AlienTimeConvertService {
    private AlienClock alienClock;
    private static final double alienSecondsPerEarthSeconds = 0.5;
    private static final LocalDateTime epochEarthTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);

    @Autowired
    private AlarmWebSocketHandler alarmWebSocketHandler;

    public AlienTimeConvertService() {
        this.alienClock = this.earthToAlien(LocalDateTime.now());
    }

    @Scheduled(fixedRate = 500)
    public void updateAlienTime(){
        this.alienClock.incrementSecond();
    }

    public void setCurrentAlienTime(AlienClock alienClock){
        this.alienClock = alienClock;
    }

    public AlienClock earthToAlien(LocalDateTime earthTime){
        long earthSeconds = earthTime.toEpochSecond(ZoneOffset.UTC) - epochEarthTime.toEpochSecond(ZoneOffset.UTC);
        long alienSeconds = (long) (earthSeconds/alienSecondsPerEarthSeconds);
        return calculateAlienTime(alienSeconds);
    }

    public LocalDateTime alienToEarth(AlienClock alienClock){
        long alienSeconds = alienClock.getSecondsSinceEpoch();
        long earthSeconds = (long) (alienSeconds * alienSecondsPerEarthSeconds);
        return epochEarthTime.plusSeconds(earthSeconds);
    }

    public AlienClock calculateAlienTime(long alienSeconds){
        AlienClock alienClock = new AlienClock(2804, 18, 31, 2, 2, 88);
        alienClock.addSeconds(alienSeconds);
        return alienClock;
    }

    public AlienClock getCurrentAlienTime(){
        return this.alienClock;
    }


}
