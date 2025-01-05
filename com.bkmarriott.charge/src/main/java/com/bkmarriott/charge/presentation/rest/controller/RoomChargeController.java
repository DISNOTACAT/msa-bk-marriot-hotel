package com.bkmarriott.charge.presentation.rest.controller;

import com.bkmarriott.charge.application.service.RoomChargeService;
import com.bkmarriott.charge.domain.RoomCharge;
import com.bkmarriott.charge.presentation.rest.dto.CreateRoomCharge;
import com.bkmarriott.charge.presentation.rest.dto.FindRoomCharge;
import com.bkmarriott.charge.presentation.rest.dto.RoomChargeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/charges")
@RequiredArgsConstructor
@RestController
public class RoomChargeController {

    private final RoomChargeService roomChargeService;

    @PostMapping
    public ResponseEntity<RoomChargeResponse> createRoomCharge(@RequestBody CreateRoomCharge.Request request) {
        RoomCharge roomCharge = roomChargeService.create(request.toDomain());
        return ResponseEntity.status(HttpStatus.CREATED).body(RoomChargeResponse.from(roomCharge));
    }

    @GetMapping
    public ResponseEntity<RoomChargeResponse> findRoomCharge(@ModelAttribute FindRoomCharge.Request request) {
        RoomCharge roomCharge = roomChargeService.findOne(request.toDomain());
        return ResponseEntity.ok().body(RoomChargeResponse.from(roomCharge));
    }

    @PatchMapping
    public ResponseEntity<RoomChargeResponse> updateRoomCharge(@RequestBody CreateRoomCharge.Request request) {
        RoomCharge roomCharge = roomChargeService.update(request.toDomain());
        return ResponseEntity.status(HttpStatus.CREATED).body(RoomChargeResponse.from(roomCharge));
    }
}
