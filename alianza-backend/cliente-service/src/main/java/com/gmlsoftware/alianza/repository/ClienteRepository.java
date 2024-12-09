package com.gmlsoftware.alianza.repository;



import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gmlsoftware.alianza.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {	

	
	@Query("""
		       SELECT c 
		       FROM Cliente c 
		       WHERE (:sharedKey IS NULL OR c.sharedKey LIKE %:sharedKey%)
		         AND (:name IS NULL OR c.name LIKE %:name%)
		         AND (:lastName IS NULL OR c.lastName LIKE %:lastName%)
		         AND (:email IS NULL OR c.email LIKE %:email%)
		         AND (:phone IS NULL OR c.phone LIKE %:phone%)
		       """)
		Page<Cliente> advancedSearch(
		        @Param("sharedKey") String sharedKey,
		        @Param("name") String name,
		        @Param("lastName") String lastName,
		        @Param("email") String email,
		        @Param("phone") String phone,
		        Pageable pageable);

	
	Optional<Cliente> findByEmail(String email);

}
