package dou888311.controller;

import dou888311.dto.SystemItemHistoryResponse;
import dou888311.dto.SystemItemImportRequest;
import dou888311.entity.SystemItem;
import dou888311.entity.SystemItemHistoryUnit;
import dou888311.error.NotFoundException;
import dou888311.service.SystemItemHistoryUnitService;
import dou888311.service.SystemItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/")
public class RestController {

    @Autowired
    SystemItemServiceImpl itemService;
    @Autowired
    SystemItemHistoryUnitService statisticService;

    @PostMapping("imports")
    public void systemItemImport(@RequestBody SystemItemImportRequest request) {
        itemService.importSystemItem(request);
    }

    @GetMapping("nodes/{id}")
    public SystemItem getSystemItem(@PathVariable String id) {
        SystemItem item = itemService.findById(id);
        if (item == null) throw new NotFoundException("Item not found");
        return item;
    }

    @GetMapping("updates")
    public SystemItemHistoryResponse getRecentlyUpdated(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime date) {
        List<SystemItemHistoryUnit> recentlyUpdated = itemService.findRecentlyUpdated(date).stream()
                .map(SystemItemHistoryUnit::new)
                .collect(Collectors.toList());
        return new SystemItemHistoryResponse(recentlyUpdated);
    }

    @GetMapping("node/{id}/history")
    public SystemItemHistoryResponse getHistory(@PathVariable String id,
                                                @RequestParam (required = false) String dateStart,
                                                @RequestParam (required = false) String dateEnd) {
        if (!itemService.isItemFound(id)) throw new NotFoundException("Item not found");

        List<SystemItemHistoryUnit> history = new ArrayList<>(statisticService.getStatistic(id, dateStart, dateEnd));
        return new SystemItemHistoryResponse(history);
    }

    @DeleteMapping("delete/{id}")
    public void deleteNode(@PathVariable String id, @RequestParam String date) {
        if (itemService.deleteNode(id, date) == null) throw new NotFoundException("Item not found");
    }
}
