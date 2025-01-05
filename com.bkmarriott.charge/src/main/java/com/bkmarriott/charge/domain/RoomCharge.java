package com.bkmarriott.charge.domain;

import com.bkmarriott.charge.domain.vo.RoomChargeId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomCharge {

    private RoomChargeId id;
    private Integer charge;

    public void updateCharge(Integer charge) {
        this.charge = charge;
    }

}
