package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import com.example.demo.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, Long> {
    List<WeatherEntity> findAllByUser(UserEntity user);

    WeatherEntity findByWeatherId(Long id);

    @Query(value = "SELECT * FROM ur_weather.weather WHERE weather.city_name= :city AND weather.user_id= :userId ORDER BY DATE DESC", nativeQuery = true)
    List<WeatherEntity> findWeatherGroupByCity(
            @Param("city") String city,
            @Param("userId") Long id);

    @Query(value =" SELECT * FROM weather WHERE user_id = 1 and date in (SELECT max(date)" +
            "FROM weather WHERE user_id =1 GROUP BY city_name)\n" +
            "ORDER BY date desc", nativeQuery = true)
    List<WeatherEntity> findCity(@Param("userId") Long id);

}
