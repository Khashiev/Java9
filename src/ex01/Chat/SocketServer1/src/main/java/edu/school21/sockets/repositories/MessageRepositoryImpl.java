package edu.school21.sockets.repositories;


import edu.school21.sockets.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component("messageRepository")
public class MessageRepositoryImpl implements MessageRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Message> messageRowMapper = new MessageRowMapper();

    @Autowired
    public MessageRepositoryImpl(@Qualifier("hikariDataSource") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Message> findById(Long id) {
        String sql = "SELECT * FROM messages WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, messageRowMapper, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Message> findAll() {
        String sql = "SELECT * FROM messages";

        return jdbcTemplate.query(sql, messageRowMapper);
    }

    @Override
    public void save(Message message) {
        String sql = "INSERT INTO messages (sender, text, time) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql, message.getSender(), message.getText(), message.getTime());
    }

    @Override
    public void update(Message message) {
        String sql = "UPDATE messages SET sender = ?, text = ?, time = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                message.getSender(), message.getText(), message.getTime(), message.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM messages WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


    private static class MessageRowMapper implements RowMapper<Message> {
        @Override
        public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
            Message message = new Message();
            message.setId(rs.getLong("id"));
            message.setSender(rs.getString("sender"));
            message.setText(rs.getString("text"));
            message.setTime(rs.getTimestamp("time").toLocalDateTime());

            return message;
        }
    }
}
