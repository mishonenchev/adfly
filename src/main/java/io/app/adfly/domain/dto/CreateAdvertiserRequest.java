package io.app.adfly.domain.dto;

import lombok.Data;

@Data
public class CreateAdvertiserRequest {
    private String summary;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
