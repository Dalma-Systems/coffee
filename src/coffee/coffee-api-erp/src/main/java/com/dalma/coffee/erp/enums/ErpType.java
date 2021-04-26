package com.dalma.coffee.erp.enums;

/**
 * Currently it is only supported SAP but in this way 
 * is structured to support easily mode integrations.
 */
public enum ErpType {
	SAP,
	;
	
	
	public static ErpType getErpType(String erp) {
		if (erp == null) {
			return null;
		}
		
		for (ErpType type : values()) {
			if (type.name().equalsIgnoreCase(erp)) {
				return type;
			}
		}
		
		return null;
	}
}
