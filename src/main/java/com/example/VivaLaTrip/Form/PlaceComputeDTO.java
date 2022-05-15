package com.example.VivaLaTrip.Form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceComputeDTO implements Comparable<PlaceComputeDTO> {

    int stay;
    int days;
    double x;
    double y;
    double slope;
    String where;

    @Override
    public int compareTo(PlaceComputeDTO place){
        if (this.slope < place.getSlope()){
            return 1;
        }else if (this.slope > place.getSlope()){
            return -1;
        }
        return 0;
    }
}
