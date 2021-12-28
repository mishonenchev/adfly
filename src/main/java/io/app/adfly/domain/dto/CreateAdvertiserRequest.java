package io.app.adfly.domain.dto;

public class CreateAdvertiserRequest {
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    private String summary;
}
