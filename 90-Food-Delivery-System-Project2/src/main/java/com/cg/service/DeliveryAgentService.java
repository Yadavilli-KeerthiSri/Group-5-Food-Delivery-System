package com.cg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.DeliveryAgent;
import com.cg.iservice.IDeliveryAgentService;
import com.cg.repository.DeliveryAgentRepository;

@Service
public class DeliveryAgentService implements IDeliveryAgentService {

    @Autowired
    private DeliveryAgentRepository deliveryAgentRepository;

    @Override
    public DeliveryAgent add(DeliveryAgent agent) {
        return deliveryAgentRepository.save(agent);
    }

    @Override
    public DeliveryAgent update(DeliveryAgent agent) {
        return deliveryAgentRepository.save(agent);
    }

    @Override
    public List<DeliveryAgent> getAll() {
        return deliveryAgentRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        deliveryAgentRepository.deleteById(id);
    }

    @Override
	public DeliveryAgent getById(Long id) {
		return deliveryAgentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Agent not found"));
	}
}
