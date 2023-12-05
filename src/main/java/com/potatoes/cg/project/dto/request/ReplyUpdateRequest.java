package com.potatoes.cg.project.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
@NoArgsConstructor(force = true)
public class ReplyUpdateRequest {

    @NotBlank
    private final String replyBody;

}
