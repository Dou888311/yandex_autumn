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
}
