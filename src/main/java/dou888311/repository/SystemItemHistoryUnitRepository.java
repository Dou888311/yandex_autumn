package dou888311.repository;

import dou888311.entity.SystemItemHistoryUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemItemHistoryUnitRepository extends JpaRepository<SystemItemHistoryUnit, Long> {

    @Query(value = "select size from system_item_history_unit where primary_key = (select max (primary_key) from system_item_history_unit where id = :id)",
        nativeQuery = true)
    Integer getLatest(@Param("id") String id);
}
