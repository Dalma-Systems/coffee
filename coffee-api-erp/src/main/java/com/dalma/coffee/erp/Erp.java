package com.dalma.coffee.erp;

import com.dalma.contract.dto.work.order.erp.ErpWorkOrder;

import java.util.List;

public interface Erp {
	List<ErpWorkOrder> integrateWorkOrders();
}
