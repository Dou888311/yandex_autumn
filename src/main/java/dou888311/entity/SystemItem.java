package dou888311.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import dou888311.dto.SystemItemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SystemItem {
    @Id
    @NotNull
    private String id;

    private String url;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime date;

    private String parentId;

    @Enumerated(EnumType.STRING)
    private SystemItemType type;

    private int size;

    @Transient
    private List<SystemItem> children;

    public static class SystemItemBuilder {
        private final SystemItem systemItem = new SystemItem();

        public SystemItemBuilder setId(String id) {
            systemItem.setId(id);
            return this;
        }

        public SystemItemBuilder setUrl(String url) {
            systemItem.setUrl(url);
            return this;
        }

        public SystemItemBuilder setDate(LocalDateTime date) {
            systemItem.setDate(date);
            return this;
        }

        public SystemItemBuilder setParentId(String parentId) {
            systemItem.setParentId(parentId);
            return this;
        }

        public SystemItemBuilder setType(SystemItemType type) {
            systemItem.setType(type);
            return this;
        }

        public SystemItemBuilder setSize(int size) {
            systemItem.setSize(size);
            return this;
        }

        public SystemItem build() {
            return systemItem;
        }
    }
}
