package org.king.quarters.domain;
// Generated by MyEclipse - Hibernate Tools

import java.util.Set;


/**
 * TabSsDept generated by MyEclipse - Hibernate Tools
 */
public class TabSsDept extends AbstractTabSsDept implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public TabSsDept() {
    }

	/** minimal constructor */
    public TabSsDept(String name) {
        super(name);        
    }
    
    /** full constructor */
    public TabSsDept(String name, String right, String status, Set tabSsQuarterses, Set tabSsManagers) {
        super(name, right, status, tabSsQuarterses, tabSsManagers);        
    }
   
}