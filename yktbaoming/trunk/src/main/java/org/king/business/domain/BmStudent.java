package org.king.business.domain;
// Generated by MyEclipse - Hibernate Tools

import java.util.Date;


/**
 * Student generated by MyEclipse - Hibernate Tools
 */
public class BmStudent extends AbstractBmStudent implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public BmStudent() {
    }

	/** minimal constructor */
    public BmStudent(String studentNo) {
        super(studentNo);        
    }
    
    /** full constructor */
    public BmStudent(String studentNo, String stuName, String gender, String enrollYear, String deptName, String majorName, String curGrade, String curClass, String idType, String idCard, String email, String updator, String updateTime,BmDepartment bmDepartment,BmMajor bmMajor) {
        super(studentNo, stuName, gender, enrollYear, deptName, majorName, curGrade, curClass, idType, idCard, email, updator, updateTime,bmDepartment,bmMajor);        
    }

	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}
   
}