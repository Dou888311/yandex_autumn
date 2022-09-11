package dou888311.service;

import dou888311.dto.SystemItemImportRequest;
import dou888311.dto.SystemItemType;
import dou888311.entity.SystemItem;
import dou888311.entity.SystemItemHistoryUnit;
import dou888311.error.ValidationException;
import dou888311.repository.SystemItemRepository;
import dou888311.support.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SystemItemServiceImpl implements SystemItemService {

    @Autowired
    SystemItemRepository itemRepository;
    @Autowired
    SystemItemHistoryUnitService historyService;
    @Autowired
    Validator validator;

    public void importSystemItem(SystemItemImportRequest request)  {
        LocalDateTime date = request.getUpdateDate();
        List<SystemItem> list = request.getItems().stream()
                .map(i -> i.toSystemItem(date))
                .collect(Collectors.toList());

        if (validator.systemItemValidate(list)) {
            itemRepository.saveAll(list);

            Set<SystemItem> units = new HashSet<>();
            for (SystemItem item : list) {
                units.addAll(findParents(item));
            }

            units.forEach(i -> i.setDate(date));
            itemRepository.saveAll(units);

            Set<SystemItemHistoryUnit> statistic = Stream.concat(units.stream(), list.stream())
                    .map(SystemItemHistoryUnit::new)
                    .collect(Collectors.toSet());

            historyService.update(statistic);
        } else {
            throw new ValidationException("Validation Failed");
        }
    }

    public Set<SystemItem> findParents(SystemItem item) {
        Set<SystemItem> parents = new HashSet<>();
        if (item.getParentId() == null) return parents;

        Optional<SystemItem> parent = itemRepository.findById(item.getParentId());
        while (parent.isPresent()) {
            parents.add(parent.get());
            if (parent.get().getParentId() == null) break;
            parent = itemRepository.findById(parent.get().getParentId());
        }
        return parents;
    }

    public SystemItem findById(String id) {
        Optional<SystemItem> optional = itemRepository.findById(id);
        if (optional.isEmpty()) return null;

        SystemItem item = optional.get();
        if (item.getType() == SystemItemType.FILE) return item;

        List<SystemItem> children = itemRepository.findAllChildrenById(id).stream()
                .map(i -> findById(i.getId()))
                .collect(Collectors.toList());

        item.setChildren(children.isEmpty() ? null : children);
        item.setSize(historyService.getLatest(id));
        return item;
    }
}
