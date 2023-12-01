package com.potatoes.cg.approval.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_expense_detail")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ExpenseDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseDetailCode;

    @Column(nullable = false)
    private String expenseAccount;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private LocalDate expenseDate;

    @Column
    private Long expensePrice;

    @Column
    private String expenseBriefs;

    public ExpenseDetail(String expenseAccount, LocalDate expenseDate, Long expensePrice, String expenseBriefs) {
        this.expenseAccount = expenseAccount;
        this.expenseDate = expenseDate;
        this.expensePrice = expensePrice;
        this.expenseBriefs = expenseBriefs;
    }

    public static ExpenseDetail of(String expenseAccount, LocalDate expenseDate, Long expensePrice, String expenseBriefs) {
        return new ExpenseDetail(
                expenseAccount,
                expenseDate,
                expensePrice,
                expenseBriefs
        );
    }


}
