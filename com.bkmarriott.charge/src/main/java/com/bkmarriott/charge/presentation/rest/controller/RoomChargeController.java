package com.bkmarriott.charge.presentation.rest.controller;

import com.bkmarriott.charge.application.service.RoomChargeService;
import com.bkmarriott.charge.domain.RoomCharge;
import com.bkmarriott.charge.presentation.rest.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("hotels")
    public ResponseEntity<List<RoomChargeResponse>> findRoomChargeByHotelIds(@ModelAttribute FindRoomChargeByHotelIds.Request request) {
        List<RoomCharge> roomChargeList = roomChargeService.findAll(request.toDomain());
        return ResponseEntity.ok().body(roomChargeList.stream().map(RoomChargeResponse::from).toList());
    }

    @GetMapping("dates")
    public ResponseEntity<List<RoomChargeResponse>> findRoomChargeByDates(@ModelAttribute FindRoomChargeByDates.Request request) {
        List<RoomCharge> roomChargeList = roomChargeService.findAll(request.toDomain());
        return ResponseEntity.ok().body(roomChargeList.stream().map(RoomChargeResponse::from).toList());
    }

    @PatchMapping
    public ResponseEntity<RoomChargeResponse> updateRoomCharge(@RequestBody CreateRoomCharge.Request request) {
        RoomCharge roomCharge = roomChargeService.update(request.toDomain());
        return ResponseEntity.status(HttpStatus.CREATED).body(RoomChargeResponse.from(roomCharge));
    }
}
