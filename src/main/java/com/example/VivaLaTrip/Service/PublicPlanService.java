package com.example.VivaLaTrip.Service;

import com.example.VivaLaTrip.Entity.Public_Plan;
import com.example.VivaLaTrip.Repository.PlanRepository;
import com.example.VivaLaTrip.Repository.PublicPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PublicPlanService {
    private final PublicPlanRepository publicPlanRepository;
    private final PlanRepository planRepository;

    /*public PublicPlanService(PublicPlanRepository publicPlanRepository) {
        this.publicPlanRepository = publicPlanRepository;
    }*/

    public void view_all_public() {
        List<Public_Plan> a = publicPlanRepository.findAll();
        log.info(String.valueOf(a));
    }

    public void to_public() {
        planRepository.getById(12L);
    }

    public void to_private() {
        publicPlanRepository.getById(12L);
    }
}
