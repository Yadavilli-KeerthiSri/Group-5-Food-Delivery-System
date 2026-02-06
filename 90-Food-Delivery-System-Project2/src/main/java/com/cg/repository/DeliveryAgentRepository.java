package com.cg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.entity.DeliveryAgent;

@Repository
public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Long>{
	Optional<DeliveryAgent> findFirstByAvailabilityTrue();
	
	List<DeliveryAgent> findAllByAvailabilityTrue();
}
