package com.svs.nace.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
//@Data
@Table(name="EconomicActivity")
public class EconomicActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_no")
    private Long order;

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

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThisItemIncludes() {
        return thisItemIncludes;
    }

    public void setThisItemIncludes(String thisItemIncludes) {
        this.thisItemIncludes = thisItemIncludes;
    }

    public String getThisItemAlsoIncludes() {
        return thisItemAlsoIncludes;
    }

    public void setThisItemAlsoIncludes(String thisItemAlsoIncludes) {
        this.thisItemAlsoIncludes = thisItemAlsoIncludes;
    }

    public String getRuling() {
        return ruling;
    }

    public void setRuling(String ruling) {
        this.ruling = ruling;
    }

    public String getThisItemexcludes() {
        return thisItemexcludes;
    }

    public void setThisItemexcludes(String thisItemexcludes) {
        this.thisItemexcludes = thisItemexcludes;
    }

    public String getRefrenceToISIC() {
        return refrenceToISIC;
    }

    public void setRefrenceToISIC(String refrenceToISIC) {
        this.refrenceToISIC = refrenceToISIC;
    }
}
