package dev.mkuwan.adminstock.infrastructure.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "UserTable")
public class UserTable {

    @Id
    private String userId;

    private String userName;
}
