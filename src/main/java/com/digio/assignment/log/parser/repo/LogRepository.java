package com.digio.assignment.log.parser.repo;

import com.digio.assignment.log.parser.model.LogStat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends CrudRepository<LogStat, Long> {

    @Query(value = "SELECT COUNT(DISTINCT a.ip) FROM logstat a where " +
            "event_time >= :start  and event_time < :end",
            nativeQuery = true)
    int findUniqueIPsCount(@Param("start") long start,
                           @Param("end") long end);

    @Query(value = "SELECT URL, count(*) FROM LOGSTAT WHERE event_time >= " +
            ":start  and event_time < :end GROUP BY URL " +
            "ORDER BY " +
            "count(*) DESC LIMIT :count", nativeQuery = true)
    List<Object[]> findMostVisitedURLs(@Param("start") long start,
                                       @Param("end") long end,
                                       @Param("count") int count);

    @Query(value = "SELECT IP, count(*) FROM LOGSTAT WHERE event_time >= " +
            ":start  and event_time < :end GROUP BY IP ORDER BY " +
            "count(*) DESC LIMIT :count", nativeQuery = true)
    List<Object[]> findMostActiveIPs(@Param("start") long start,
                                     @Param("end") long end,
                                     @Param("count") int count);

}
