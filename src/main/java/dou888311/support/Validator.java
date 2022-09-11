package dou888311.support;

import dou888311.dto.SystemItemType;
import dou888311.entity.SystemItem;
import dou888311.repository.SystemItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

@Component
public class Validator {

    @Autowired
    SystemItemRepository itemRepository;

    public boolean systemItemValidate(List<SystemItem> list) {
        for (SystemItem item : list) {
            if (!parentIsFolder(item)) return false;
            if (!isUrlNullForFolder(item)) return false;
            if (!isUrlLessThan255ForFile(item)) return false;
        }
        return true;
    }

    public boolean parentIsFolder(SystemItem item) {
        if (item.getParentId() != null) {
            if (itemRepository.existsById(item.getParentId())) {
                Optional<SystemItem> temp = itemRepository.findById(item.getParentId());
                return temp.orElseThrow(ValidationException::new).getType() != SystemItemType.FILE;
            }
        }
        return true;
    }

    public static boolean isUrlNullForFolder(SystemItem item) {
        if (item.getType() == SystemItemType.FOLDER) {
            return item.getUrl() == null;
        }
        return true;
    }

    public static boolean isUrlLessThan255ForFile(SystemItem item) {
        if (item.getType() == SystemItemType.FILE) {
            return item.getUrl().length() <= 255;
        }
        return true;
    }
}
