package com.example.VivaLaTrip.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PublicPlanService {
    /*private final PublicPlanRepository publicPlanRepository;
    private final PlanRepository planRepository;

    *//*public PublicPlanService(PublicPlanRepository publicPlanRepository) {
        this.publicPlanRepository = publicPlanRepository;
    }*//*

    public void view_all_public() {
        List<PublicPlan> a = publicPlanRepository.findAll();
        log.info(String.valueOf(a));
    }

    public void to_public() {
        planRepository.getById(12L);
    }

    public void to_private() {
        publicPlanRepository.getById(12L);
    }*/
}
