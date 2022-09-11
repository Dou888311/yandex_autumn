package dou888311.service;

import dou888311.dto.SystemItemImportRequest;
import dou888311.entity.SystemItem;

public interface SystemItemService {

    void importSystemItem(SystemItemImportRequest request);
    SystemItem deleteNode(String id);
    SystemItem findById(String id);
}
