package com.gft.manager.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class VideoExternalUrlRequest {
    @NotEmpty
    private String videoId;
    @NotEmpty
    private String url;
}
