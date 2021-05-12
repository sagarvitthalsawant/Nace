package com.svs.nace.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="EconomicActivity")
public class EconomicActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long economicActivityId;

    @Column(name = "order_no")
    private Long orderNo;

    @Column(name = "level")
    private Long level;

    @Column(name = "code")
    private String code = null;

    @Column(name = "parent")
    private String parent = null;

    @Column(name = "description")
    @Lob
    private String description = null;

    @Column(name = "thisItemIncludes")
    @Lob
    private String thisItemIncludes = null;

    @Column(name = "thisItemAlsoIncludes")
    @Lob
    private String thisItemAlsoIncludes = null;

    @Column(name = "ruling")
    @Lob
    private String ruling = null;

    @Column(name = "thisItemexcludes")
    @Lob
    private String thisItemexcludes = null;

    @Column(name = "refrenceToISIC")
    @Lob
    private String refrenceToISIC = null;

}
