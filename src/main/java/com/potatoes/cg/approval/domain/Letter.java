package com.potatoes.cg.approval.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "tbl_letter")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long letterCode;

    @Column(nullable = false)
    private String letterBody;

    @OneToOne
    @JoinColumn(name = "approvalCode")
    private Approval approval;

    public Letter(String letterBody) {
        this.letterBody = letterBody;
    }

    public static Letter of(String letterBody) {
        return new Letter(
                letterBody
        );
    }
}
