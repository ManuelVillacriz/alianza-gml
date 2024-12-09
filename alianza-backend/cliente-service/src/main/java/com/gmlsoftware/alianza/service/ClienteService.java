package com.gmlsoftware.alianza.service;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gmlsoftware.alianza.common.service.CommonService;
import com.gmlsoftware.alianza.entity.Cliente;

public interface ClienteService extends CommonService<Cliente> {
	
	
	Page<Cliente> advancedSearch(String sharedKey, String name, String lastName, String email, String phone, Pageable pageable);

	public String generateCsv();

}

