package com.cg.iservice;

import java.util.List;

import com.cg.dto.DeliveryAgentDto;

public interface IDeliveryAgentService {

    DeliveryAgentDto add(DeliveryAgentDto dto);

    DeliveryAgentDto update(DeliveryAgentDto dto);

    DeliveryAgentDto getById(Long id);

    List<DeliveryAgentDto> getAll();

    void delete(Long id);
}