package com.prashanth.spring.distributed.locks.controller;

import com.prashanth.spring.distributed.locks.model.Reservation;
import com.prashanth.spring.distributed.locks.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@RestController
@RequiredArgsConstructor
public class LockedResourceRestController {

    private final LockRegistry lockRegistry;
    private final ReservationRepository reservationRepository;

    @SneakyThrows
    @GetMapping("/update/{id}/{name}/{time}")
    public Reservation update(@PathVariable Integer id, @PathVariable String name, @PathVariable Long time) {
        String key = Integer.toString(id);
        Lock lock = lockRegistry.obtain(key);
        boolean lockAcquired = lock.tryLock(1, TimeUnit.SECONDS);
        if (lockAcquired) {
            try {
                doUpdateFor(id, name);
                Thread.sleep(time);
            } finally {
                lock.unlock();
            }
        }
        return reservationRepository.findById(id).get();
    }

    private void doUpdateFor(Integer id, String name) {
        reservationRepository.findById(id).ifPresent(r -> {
            r.setName(name);
            reservationRepository.update(r);
        });
    }
}
