package com.potatoes.cg.member.dto.response;

import com.potatoes.cg.member.domain.Dept;
import com.potatoes.cg.member.domain.History;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class DeptResponse {

    private final Long deptCode;
    private final String deptName;
    private final String deptDescribe;

    public static DeptResponse from( final Dept dept ) {

        return new DeptResponse(
                dept.getDeptCode(),
                dept.getDeptName(),
                dept.getDeptDescribe()
        );

    }

}
