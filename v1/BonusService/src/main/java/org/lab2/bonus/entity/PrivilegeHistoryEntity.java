package org.lab2.bonus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.lab2.enums.OperationType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "privilege_history")
public class PrivilegeHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "privilege_id")
    private PrivilegeEntity privilege;

    @Column(name = "ticket_uid", nullable = false)
    private UUID ticketUid;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime datetime;

    @Column(name = "balance_diff", nullable = false)
    private Integer balanceDiff;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false, length = 20)
    private OperationType operationType;

    public PrivilegeHistoryEntity() {
    }

    public PrivilegeHistoryEntity(Integer id, PrivilegeEntity privilege, UUID ticketUid,
                                  LocalDateTime datetime, Integer balanceDiff, OperationType operationType) {
        this.id = id;
        this.privilege = privilege;
        this.ticketUid = ticketUid;
        this.datetime = datetime;
        this.balanceDiff = balanceDiff;
        this.operationType = operationType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PrivilegeEntity getPrivilege() {
        return privilege;
    }

    public void setPrivilege(PrivilegeEntity privilege) {
        this.privilege = privilege;
    }

    public UUID getTicketUid() {
        return ticketUid;
    }

    public void setTicketUid(UUID ticketUid) {
        this.ticketUid = ticketUid;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public Integer getBalanceDiff() {
        return balanceDiff;
    }

    public void setBalanceDiff(Integer balanceDiff) {
        this.balanceDiff = balanceDiff;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
}
