package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.DeliveryAgentDto;
import com.cg.entity.DeliveryAgent;
import com.cg.iservice.IDeliveryAgentService;
import com.cg.mapper.DeliveryAgentMapper;
import com.cg.repository.DeliveryAgentRepository;
import com.cg.repository.OrderRepository;

@Service
public class DeliveryAgentService implements IDeliveryAgentService {

    @Autowired
    private DeliveryAgentRepository deliveryAgentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public DeliveryAgentDto add(DeliveryAgentDto dto) {
        DeliveryAgent entity = DeliveryAgentMapper.fromCreateDto(
                dto,
                ids -> orderRepository.findAllById(ids)
        );
        DeliveryAgent saved = deliveryAgentRepository.save(entity);
        return DeliveryAgentMapper.toDto(saved);
    }

    @Override
    public DeliveryAgentDto update(DeliveryAgentDto dto) {
        // Load existing to ensure it exists (optional)
        DeliveryAgent existing = deliveryAgentRepository.findById(dto.getAgentId())
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        DeliveryAgentMapper.applyUpdate(dto, existing, ids -> orderRepository.findAllById(ids));
        DeliveryAgent saved = deliveryAgentRepository.save(existing);
        return DeliveryAgentMapper.toDto(saved);
    }

    @Override
    public List<DeliveryAgentDto> getAll() {
        return deliveryAgentRepository.findAll()
                .stream()
                .map(DeliveryAgentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        deliveryAgentRepository.deleteById(id);
    }

    @Override
    public DeliveryAgentDto getById(Long id) {
        DeliveryAgent entity = deliveryAgentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        return DeliveryAgentMapper.toDto(entity);
    }
}