package com.potatoes.cg.approval.domain;

import com.potatoes.cg.approval.domain.type.expenseType.ExpenseStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "tbl_expense")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseCode;

    @Column
    private String expenseNote;

    @Enumerated(value = STRING)
    @Column
    private ExpenseStatusType expenseStatus;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "approvalCode")
    private Approval approval;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "expenseCode")
    private List<ExpenseDetail> expenseDetail;

    public Expense(String expenseNote, ExpenseStatusType expenseStatus,
                   List<ExpenseDetail> expenseDetail, Approval approval) {
        this.expenseNote = expenseNote;
        this.expenseStatus = expenseStatus;
        this.expenseDetail = expenseDetail;
        this.approval = approval;
    }

    public static Expense of(String expenseNote, ExpenseStatusType expenseStatus,
                             List<ExpenseDetail> expenseDetail, Approval approval) {

        return new Expense(
                expenseNote,
                expenseStatus,
                expenseDetail,
                approval
        );
    }
}
