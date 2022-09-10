package dou888311.entity;

import dou888311.dto.SystemItemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SystemItemHistoryUnit {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long primary_key;

    @NotNull
    private String id;

    private String url;

    private String parentId;

    @Enumerated(EnumType.STRING)
    private SystemItemType type;

    private int size;

    private LocalDateTime date;


    public SystemItemHistoryUnit(SystemItem item) {
        this.id = item.getId();
        this.url = item.getUrl();
        this.parentId = item.getParentId();
        this.type = item.getType();
        this.size = item.getSize();
        this.date = item.getDate();
    }
}
