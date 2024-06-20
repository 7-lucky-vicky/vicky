package com.sparta.delivery_app.domain.menu.entity;

import com.sparta.delivery_app.domain.commen.BaseTimeEntity;
import com.sparta.delivery_app.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menu")
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String menuName;

    @Column(nullable = false)
    private Long menuPrice;

    @Column(nullable = false)
    private String menuInfo;

    @Column
    private String menuImagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MenuStatus menuStatus;

    @Builder
    public Menu(Store store, String menuName, Long menuPrice, String menuInfo, String menuImagePath, MenuStatus menuStatus) {
        this.store = store;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuInfo = menuInfo;
        this.menuImagePath = menuImagePath;
        this.menuStatus = menuStatus;
    }
}
