package com.dalma.coffee.erp.factory;

import com.dalma.coffee.erp.Erp;
import com.dalma.coffee.erp.Sap;
import com.dalma.coffee.erp.enums.ErpType;
import com.dalma.coffee.erp.exception.ErpNotSupportedException;
import org.springframework.stereotype.Component;

@Component
public class ErpFactory {
	
	private final Sap sap;

	public ErpFactory(Sap sap) {
		this.sap = sap;
	}
	
	public Erp getErp(String erp) {
		ErpType type = ErpType.getErpType(erp);
		
		// Transform in a switch-case when is supported
		// more ERPs to increase reliability
		if (ErpType.SAP.equals(type)) {
			return sap;
		}
		
		throw new ErpNotSupportedException(erp);
	}
}
