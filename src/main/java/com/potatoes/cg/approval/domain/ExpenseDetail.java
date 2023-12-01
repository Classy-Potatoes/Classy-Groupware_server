package com.potatoes.cg.approval.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tbl_expense_detail")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ExpenseDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseDetailCode;

//    @Column
//    @NotNull
//    private String exp
}
