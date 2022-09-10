package dou888311.repository;

import dou888311.entity.SystemItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemItemRepository extends JpaRepository<SystemItem, String> {

    boolean existsById(String id);

    @Query(value = "SELECT * FROM system_item WHERE parent_id = :id", nativeQuery = true)
    List<SystemItem> findAllChildrenById(@Param("id") String id);
}
