package com.potatoes.cg.project.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.NotBlank;


@RequiredArgsConstructor
@Getter
@NoArgsConstructor(force = true)
public class PostUpdateRequest {

    @NotBlank
    private final String postTitle;

    @NotBlank
    private final String postBody;

//    @Min(value = 1)
//    private final Long projectCode;
// 필요한 Setter 메서드 추가


}
