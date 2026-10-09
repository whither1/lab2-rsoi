package org.lab2.bonus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.lab2.enums.PrivilegeStatus;

@Entity
@Table(name = "privilege")
public class PrivilegeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true, length = 80)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 80)
    private PrivilegeStatus status = PrivilegeStatus.BRONZE;

    @Column(name = "balance")
    private Integer balance = 0;

    public PrivilegeEntity() {
    }

    public PrivilegeEntity(Integer id, String username, PrivilegeStatus status, Integer balance) {
        this.id = id;
        this.username = username;
        this.status = status;
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PrivilegeStatus getStatus() {
        return status;
    }

    public void setStatus(PrivilegeStatus status) {
        this.status = status;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
