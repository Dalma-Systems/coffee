package com.dalma.coffee.erp;

import com.dalma.coffee.erp.api.ErpWorkOrder;

import java.util.List;

public interface Erp {
	List<ErpWorkOrder> integrateWorkOrders();
}
