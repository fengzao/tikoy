package com.cokebook.tools.tikoy.container;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @date 2025/1/31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobSnapshotRunProps {
    private String group;
    private Map<String, Prop> props;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Prop {
        private Boolean enable;
        private Long offset;

    }


}