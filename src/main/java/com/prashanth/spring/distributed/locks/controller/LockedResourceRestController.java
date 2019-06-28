package com.prashanth.spring.distributed.locks.controller;

import com.prashanth.spring.distributed.locks.model.Reservation;
import com.prashanth.spring.distributed.locks.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LockedResourceRestController {

    private final ReservationRepository reservationRepository;

    @GetMapping("/update/{id}/{name}/{time}")
    Reservation update(@PathVariable Integer id, @PathVariable String name, @PathVariable Long time) {
        return doUpdateFor(id, name);
    }

    Reservation doUpdateFor(Integer id, String name) {
        reservationRepository.findById(id).ifPresent(r -> {
            r.setName(name);
            reservationRepository.update(r);
        });
        return reservationRepository.findById(id).get();
    }
}
