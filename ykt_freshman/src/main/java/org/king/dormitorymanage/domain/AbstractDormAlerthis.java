package org.king.dormitorymanage.domain;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.EqualsBuilder;



/**
 * AbstractDormAlerthis generated by MyEclipse - Hibernate Tools
 */

public abstract class AbstractDormAlerthis extends org.king.framework.domain.BaseObject implements java.io.Serializable {


    // Fields    

     private String alertId;
     private String studentId;
     private String dormitoryIdO;
     private String classIdO;
     private String dormitoryIdN;
     private String classIdN;
     private String operatorId;
     private String operatorTime;


    // Constructors

    /** default constructor */
    public AbstractDormAlerthis() {
    }

    
    /** full constructor */
    public AbstractDormAlerthis(String studentId, String dormitoryIdO, String classIdO, String dormitoryIdN, String classIdN, String operatorId, String operatorTime) {
        this.studentId = studentId;
        this.dormitoryIdO = dormitoryIdO;
        this.classIdO = classIdO;
        this.dormitoryIdN = dormitoryIdN;
        this.classIdN = classIdN;
        this.operatorId = operatorId;
        this.operatorTime = operatorTime;
    }

   
    // Property accessors

    public String getAlertId() {
        return this.alertId;
    }
    
    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getStudentId() {
        return this.studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDormitoryIdO() {
        return this.dormitoryIdO;
    }
    
    public void setDormitoryIdO(String dormitoryIdO) {
        this.dormitoryIdO = dormitoryIdO;
    }

    public String getClassIdO() {
        return this.classIdO;
    }
    
    public void setClassIdO(String classIdO) {
        this.classIdO = classIdO;
    }

    public String getDormitoryIdN() {
        return this.dormitoryIdN;
    }
    
    public void setDormitoryIdN(String dormitoryIdN) {
        this.dormitoryIdN = dormitoryIdN;
    }

    public String getClassIdN() {
        return this.classIdN;
    }
    
    public void setClassIdN(String classIdN) {
        this.classIdN = classIdN;
    }

    public String getOperatorId() {
        return this.operatorId;
    }
    
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorTime() {
        return this.operatorTime;
    }
    
    public void setOperatorTime(String operatorTime) {
        this.operatorTime = operatorTime;
    }


	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(1836337323, 2020057457).append(
				this.studentId).append(this.dormitoryIdO).append(this.classIdO)
				.append(this.alertId).append(this.dormitoryIdN).append(
						this.operatorId).append(this.classIdN).append(
						this.operatorTime).toHashCode();
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("classIdO", this.classIdO).append("operatorTime",
						this.operatorTime).append("alertId", this.alertId)
				.append("classIdN", this.classIdN).append("studentId",
						this.studentId).append("operatorId", this.operatorId)
				.append("dormitoryIdO", this.dormitoryIdO).append(
						"dormitoryIdN", this.dormitoryIdN).toString();
	}


	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof AbstractDormAlerthis)) {
			return false;
		}
		AbstractDormAlerthis rhs = (AbstractDormAlerthis) object;
		return new EqualsBuilder().append(this.studentId, rhs.studentId)
				.append(this.dormitoryIdO, rhs.dormitoryIdO).append(
						this.classIdO, rhs.classIdO).append(this.alertId,
						rhs.alertId)
				.append(this.dormitoryIdN, rhs.dormitoryIdN).append(
						this.operatorId, rhs.operatorId).append(this.classIdN,
						rhs.classIdN).append(this.operatorTime,
						rhs.operatorTime).isEquals();
	}
   








}