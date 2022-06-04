package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Places;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//Places테이블을 직접적으로 CRUD를 처리할 수 있는 Repositoty
@Repository
public interface MapRepository extends JpaRepository<Places, Long> {

    //모든 장소 값을 호출함
    List<Places> findAll();

    //사용자로부터 입력받은 단어를(Places테이블의 주소 컬럼 조회) 바탕으로 일치하는 장소 값들을 호출 함
    List<Places> findByAddressNameContains(String word, Sort sort);

    //x,y값의 사이에있는 Places들을 뽑아옴(인기순 정렬)
    List<Places> findByXBetweenAndYBetween(double x_min, double x_max, double Y_min, double Y_max, Sort sort);

    //Places가 가지고 있는 고유 Id값을 바탕으로 일치하는 Places를 뽑아옴
    Places findById(String id);

    //장소 저장.
    Places save(Places places);
}

