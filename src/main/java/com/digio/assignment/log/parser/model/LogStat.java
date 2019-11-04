package com.digio.assignment.log.parser.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "logstat")
@Entity
public class LogStat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String url;

    private String ip;

    @Column(name = "event_time")
    private long eventTime;

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public String getIp() {

        return ip;
    }

    public void setIp(String ip) {

        this.ip = ip;
    }

    public long getEventTime() {

        return eventTime;
    }

    public void setEventTime(long eventTime) {

        this.eventTime = eventTime;
    }
}
