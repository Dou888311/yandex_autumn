package dou888311.service;

import dou888311.dto.SystemItemType;
import dou888311.entity.SystemItemHistoryUnit;
import dou888311.error.ValidationException;
import dou888311.repository.SystemItemHistoryUnitRepository;
import dou888311.repository.SystemItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SystemItemHistoryUnitService {

    @Autowired
    SystemItemRepository itemRepository;

    @Autowired
    SystemItemHistoryUnitRepository historyRepository;

    public void update(Set<SystemItemHistoryUnit> statistics) {

        statistics.forEach(e -> e.setSize(getSize(e)));

        historyRepository.saveAll(statistics);
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

    public List<SystemItemHistoryUnit> getStatistic(String id, String dateStart, String dateEnd) {

        if (Objects.equals(dateStart, null) && Objects.equals(dateEnd, null)) {
            return historyRepository.getById(id);
        } else if (Objects.equals(dateStart, null)) {
            return historyRepository.getByIdBefore(id, convert(dateEnd));
        } else if (Objects.equals(dateEnd, null)) {
            return historyRepository.getByIdAfter(id, convert(dateStart));
        } else {
            return historyRepository.getStatistic(id, convert(dateStart), convert(dateEnd));
        }
    }

    public LocalDateTime convert(String dateString) {
        LocalDateTime date;
        try {
            Instant ins = Instant.parse(dateString);
            date = LocalDateTime.ofInstant(ins, ZoneOffset.UTC);
        } catch (Exception e) {
            throw new ValidationException("Validation Failed");
        }
        return date;
    }
}
