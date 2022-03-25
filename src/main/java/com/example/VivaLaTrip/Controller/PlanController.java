package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Form.PlanResponseDto;
import com.example.VivaLaTrip.Form.PlanSaveRequestDto;
import com.example.VivaLaTrip.Form.PlanUpdateRequestDto;
import com.example.VivaLaTrip.Service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class PlanController {
    private final PlanService planService;

    @Autowired
    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @PostMapping("/plan_savepro")
    public String save(@RequestParam("content")String content, @RequestParam("title")String title,@RequestParam("author")String author,@RequestParam("place_list")String place_list){
        PlanSaveRequestDto requestDto = new PlanSaveRequestDto(title, content, author, place_list);
//        System.out.println("@RequestParam으로 받은 content"+content);
        planService.save(requestDto);
        return "public_plan";
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PlanUpdateRequestDto requestDto){
        return planService.update(id,requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PlanResponseDto findById (@PathVariable Long id){
        return  planService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id){
        planService.delete(id);
        return id;
    }
}
