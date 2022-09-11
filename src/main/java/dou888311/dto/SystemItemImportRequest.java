package dou888311.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemItemImportRequest {

    @NotNull
    private List<SystemItemImport> items;
    @NotNull
    private LocalDateTime updateDate;
}
