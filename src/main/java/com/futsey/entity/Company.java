package com.futsey.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "users")
@EqualsAndHashCode(of = "name")
@Builder
public class Company extends AuditableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
//    @org.hibernate.annotations.OrderBy(clause = "username DESC, lastname ASC")
    @OrderBy(value = "personalInfo.lastname")
    private Set<User> users = new HashSet<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "company_locale")
//    @AttributeOverride(name = "lang", column = @Column(name = "lang"))
    private List<LocaleInfo> localeList = new ArrayList();

    public void addUser(User user) {
        users.add(user);
        user.setCompany(this);
    }
}
