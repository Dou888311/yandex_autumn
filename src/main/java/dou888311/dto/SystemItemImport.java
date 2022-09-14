package dou888311.dto;

import dou888311.entity.SystemItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemItemImport {

    @NotNull
    private String id;
    private String url;
    private String parentId;
    private SystemItemType type;
    private int size;

    public SystemItem toSystemItem(LocalDateTime date) {
        return new SystemItem.SystemItemBuilder()
                .setId(id)
                .setUrl(url)
                .setParentId(parentId)
                .setType(type)
                .setSize(size)
                .setDate(date)
                .build();
    }

    public SystemItemImport(SystemItem item) {
        id = item.getId();
        url = item.getUrl();
        parentId = item.getParentId();
        type = item.getType();
        size = item.getSize();
    }

    public static class SystemItemImportBuilder {
        private final SystemItemImport item = new SystemItemImport();

        public SystemItemImportBuilder id(String id) {
            item.id = id;
            return this;
        }

        public SystemItemImportBuilder url(String url) {
            item.url = url;
            return this;
        }

        public SystemItemImportBuilder parentId(String parentId) {
            item.parentId = parentId;
            return this;
        }

        public SystemItemImportBuilder type(SystemItemType type) {
            item.type = type;
            return this;
        }

        public SystemItemImportBuilder size(int size) {
            item.size = size;
            return this;
        }

        public SystemItemImport build() {
            return item;
        }
    }
}
