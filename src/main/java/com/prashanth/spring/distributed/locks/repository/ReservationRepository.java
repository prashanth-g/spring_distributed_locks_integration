package com.prashanth.spring.distributed.locks.repository;

import com.prashanth.spring.distributed.locks.model.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {
    private final RowMapper<Reservation> rowMapper = (rs, i) -> new Reservation(rs.getInt("id"),
            rs.getString("name"));

    private final JdbcTemplate jdbcTemplate;

    Optional<Reservation> findById(Integer id) {
        List<Reservation> reservationList = jdbcTemplate.query("select * from reservation where id = ?", this.rowMapper, id);
        if(reservationList.size() > 0) {
            return Optional.ofNullable(reservationList.iterator().next());
        }
        return Optional.empty();
    }
}
