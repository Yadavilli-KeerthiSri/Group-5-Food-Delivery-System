package com.cg.iservice;

import java.util.List;

import com.cg.entity.DeliveryAgent;

public interface IDeliveryAgentService {
	DeliveryAgent add(DeliveryAgent agent);

    DeliveryAgent update(DeliveryAgent agent);

    List<DeliveryAgent> getAll();

    void delete(Long id);
    
    public DeliveryAgent getById(Long id);
}
