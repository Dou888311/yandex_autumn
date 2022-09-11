package dou888311.dto;

import dou888311.entity.SystemItemHistoryUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemItemHistoryResponse {

    private List<SystemItemHistoryUnit> items;
}
