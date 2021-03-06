/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Fri Dec 09 14:17:05 CST 2005 by MyEclipse Hibernate Tool.
 */
package com.kingstargroup.advquery.reportshopbalance;

import java.io.Serializable;

/**
 * A class representing a composite primary key id for the T_TIF_REPORT_SHOP_BALANCE
 * table.  This object should only be instantiated for use with instances 
 * of the TTifReportShopBalance class.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized 
 * by MyEclipse Hibernate tool integration.
 */
public class TTifReportShopBalanceKey
    implements Serializable
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private volatile int hashValue = 0;

    /** The value of the BALANCE_DATE component of this composite id. */
    private java.lang.String balanceDate;

    /** The value of the SHOP_ID component of this composite id. */
    private java.lang.Integer shopId;

    /**
     * Simple constructor of TTifReportShopBalanceKey instances.
     */
    public TTifReportShopBalanceKey()
    {
    }

    /**
     * Returns the value of the balanceDate property.
     * @return java.lang.String
     */
    public java.lang.String getBalanceDate()
    {
        return balanceDate;
    }

    /**
     * Sets the value of the balanceDate property.
     * @param balanceDate
     */
    public void setBalanceDate(java.lang.String balanceDate)
    {
        hashValue = 0;
        this.balanceDate = balanceDate;
    }

    /**
     * Returns the value of the shopId property.
     * @return java.lang.Integer
     */
    public java.lang.Integer getShopId()
    {
        return shopId;
    }

    /**
     * Sets the value of the shopId property.
     * @param shopId
     */
    public void setShopId(java.lang.Integer shopId)
    {
        hashValue = 0;
        this.shopId = shopId;
    }

    /**
     * Implementation of the equals comparison on the basis of equality of the id components.
     * @param rhs
     * @return boolean
     */
    public boolean equals(Object rhs)
    {
        if (rhs == null)
            return false;
        if (! (rhs instanceof TTifReportShopBalanceKey))
            return false;
        TTifReportShopBalanceKey that = (TTifReportShopBalanceKey) rhs;
        if (this.getBalanceDate() == null || that.getBalanceDate() == null)
        {
            return false;
        }
        if (! this.getBalanceDate().equals(that.getBalanceDate()))
        {
            return false;
        }
        if (this.getShopId() == null || that.getShopId() == null)
        {
            return false;
        }
        if (! this.getShopId().equals(that.getShopId()))
        {
            return false;
        }
        return true;
    }

    /**
     * Implementation of the hashCode method conforming to the Bloch pattern with
     * the exception of array properties (these are very unlikely primary key types).
     * @return int
     */
    public int hashCode()
    {
        if (this.hashValue == 0)
        {
            int result = 17;
            int balanceDateValue = this.getBalanceDate() == null ? 0 : this.getBalanceDate().hashCode();
            result = result * 37 + balanceDateValue;
            int shopIdValue = this.getShopId() == null ? 0 : this.getShopId().hashCode();
            result = result * 37 + shopIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }
}
