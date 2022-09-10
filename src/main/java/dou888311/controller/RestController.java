package dou888311.controller;

import dou888311.dto.SystemItemImportRequest;
import dou888311.entity.SystemItem;
import dou888311.error.NotFoundException;
import dou888311.service.SystemItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/")
public class RestController {

    @Autowired
    SystemItemServiceImpl itemService;

    @PostMapping("imports")
    public void systemItemImport(@RequestBody  SystemItemImportRequest request) throws Exception {
        itemService.importSystemItem(request);
    }

    @GetMapping("nodes/{id}")
    public SystemItem getSystemItem(@PathVariable String id) {
        SystemItem item = itemService.findById(id);
        if (item == null) throw new NotFoundException("Item not found");
        return item;
    }
}
