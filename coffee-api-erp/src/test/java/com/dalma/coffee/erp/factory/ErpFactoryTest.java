package com.dalma.coffee.erp.factory;

import com.dalma.coffee.erp.Sap;
import com.dalma.coffee.erp.exception.ErpNotSupportedException;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ErpFactoryTest {
	@Test
	public void testErpFactory() {
		Sap sap = new Sap(null, null);
		ErpFactory factory = new ErpFactory(sap);

		assertEquals(sap, factory.getErp("sap"));
		assertThrows(ErpNotSupportedException.class, () -> {
			factory.getErp(Strings.EMPTY);
		});
	}
}
