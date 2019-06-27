package com.prashanth.spring.distributed.locks.repository;

import com.prashanth.spring.distributed.locks.model.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {
    private final RowMapper<Reservation> rowMapper = (rs, i) -> new Reservation(rs.getInt("id"),
            rs.getString("name"));

    private final JdbcTemplate jdbcTemplate;

    public Optional<Reservation> findById(Integer id) {
        List<Reservation> reservationList = jdbcTemplate.query("select * from reservation where id = ?", this.rowMapper, id);
        if(reservationList.size() > 0) {
            return Optional.ofNullable(reservationList.iterator().next());
        }
        return Optional.empty();
    }

    public Reservation update(Reservation reservation) {
        return  jdbcTemplate.execute("update reservation set name = ? where id = ?", new PreparedStatementCallback<Reservation>() {
            @Override
            public Reservation doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, reservation.getName());
                ps.setInt(2, reservation.getId());
                return findById(reservation.getId()).get();
            }
        });
    }
}
