package com.example.VivaLaTrip.Service;

import com.example.VivaLaTrip.Entity.Plan;
import com.example.VivaLaTrip.Form.PlanListResponseDto;
import com.example.VivaLaTrip.Form.PlanResponseDto;
import com.example.VivaLaTrip.Form.PlanSaveRequestDto;
import com.example.VivaLaTrip.Form.PlanUpdateRequestDto;
import com.example.VivaLaTrip.Repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    @Transactional
    public Long save(PlanSaveRequestDto requestDto){
        return planRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PlanUpdateRequestDto requestDto){
        Plan plan = planRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));

        plan.update(requestDto.getTitle(),requestDto.getContent(),requestDto.getPlace_list());
        return id;
    }
    public PlanResponseDto findById(Long id){
        Plan entity = planRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        return new PlanResponseDto(entity);
    }

    @Transactional(readOnly = true)//트랜젝션 범위는 유지 하고 조회만 가능하게 - 속도빠름
    public List<PlanListResponseDto> findAllDesc(){
        return planRepository.findAllDesc().stream().map(PlanListResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id){
        Plan plan = planRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        planRepository.delete(plan);//jpa꺼 사용
    }
}