package dou888311.service;

import dou888311.dto.SystemItemType;
import dou888311.entity.SystemItemHistoryUnit;
import dou888311.repository.SystemItemHistoryUnitRepository;
import dou888311.repository.SystemItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SystemItemHistoryUnitService {

    @Autowired
    SystemItemRepository itemRepository;

    @Autowired
    SystemItemHistoryUnitRepository historyRepository;

    public void update(Set<SystemItemHistoryUnit> statistics) {
        Set<SystemItemHistoryUnit> units = new HashSet<>();
        Set<SystemItemHistoryUnit> folders = new HashSet<>();
        for (var unit : statistics) {
            if (unit.getType() == SystemItemType.FILE) {
                unit.setSize(getSize(unit));
                units.add(unit);
            } else {
                folders.add(unit);
            }
        }
        folders.forEach(e -> e.setSize(getSize(e)));

        historyRepository.saveAll(units);
        historyRepository.saveAll(folders);
    }

    private int getSize(SystemItemHistoryUnit unit) {
        int size = 0;
        if (unit.getType() == SystemItemType.FOLDER) {
            List<SystemItemHistoryUnit> children = itemRepository.findAllChildrenById(unit.getId())
                    .stream()
                    .map(SystemItemHistoryUnit::new)
                    .collect(Collectors.toList());
            for (var child : children) {
                size += getSize(child);
            }
        } else {
            return unit.getSize();
        }
        return size;
    }

    public int getLatest(String id) {
        Optional<Integer> optional = Optional.of(historyRepository.getLatest(id));
        return optional.orElse(0);
    }

    public List<SystemItemHistoryUnit> getStatistic(String id, LocalDateTime dateStart, LocalDateTime dateEnd) {
        return historyRepository.getStatistic(id, dateStart, dateEnd);
    }
}
