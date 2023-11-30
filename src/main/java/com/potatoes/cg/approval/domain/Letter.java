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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "approvalCode")
    private Approval approval;

    public Letter(String letterBody, Approval approval) {
        this.letterBody = letterBody;
        this.approval = approval;
    }

    public static Letter of(final String letterBody, Approval approval) {
        return new Letter(
                letterBody,
                approval

        );
    }

}
