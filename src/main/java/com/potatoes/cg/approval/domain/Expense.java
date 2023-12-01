package com.potatoes.cg.approval.domain;

import com.potatoes.cg.approval.domain.type.expenseType.ExpenseStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

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

    @Column
    private ExpenseStatusType expenseStatus;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "approvalCode")
    private Approval approval;

//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "expenseCode")

}
