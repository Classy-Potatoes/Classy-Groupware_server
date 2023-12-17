package com.potatoes.cg.project.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@NoArgsConstructor(force = true)
public class TaskCheckRequest {

    private final String  taskStatus;
}
