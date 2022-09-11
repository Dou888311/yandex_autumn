package dou888311.repository;

import dou888311.entity.SystemItemHistoryUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemItemHistoryUnitRepository extends JpaRepository<SystemItemHistoryUnit, Long> {

    @Query(value = "SELECT size FROM system_item_history_unit WHERE primary_key = (SELECT max (primary_key) FROM system_item_history_unit WHERE id = :id)",
        nativeQuery = true)
    Integer getLatest(@Param("id") String id);

    @Query(value = "DELETE FROM system_item_history_unit WHERE id = :id", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteAllBySystemItemId(@Param("id") String id);

    @Query(value = "SELECT * FROM system_item_history_unit WHERE id = :id AND date >= :dateStart AND date < :dateEnd", nativeQuery = true)
    List<SystemItemHistoryUnit> getStatistic(@Param("id") String id,
                                             @Param("dateStart") LocalDateTime dateStart,
                                             @Param("dateEnd") LocalDateTime dateEnd);
}
