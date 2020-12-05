package com.pandeka.TemanNgoding.database;

import com.pandeka.TemanNgoding.model.JointEvents;
import com.pandeka.TemanNgoding.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class DaoImpl implements Dao {

    //table user
    private final static String USER_TABLE = "tbl_user";
    private final static String SQL_SELECT_ALL = "SELECT id, user_id, line_id, display_name FROM " + USER_TABLE;
    private final static String SQL_GET_BY_USER_ID = SQL_SELECT_ALL + " WHERE LOWER(user_id) LIKE LOWER(?);";
    private final static String SQL_REGISTER = "INSERT INTO " + USER_TABLE + " (user_id, line_id, display_name) VALUES (?, ?, ?);";

    //table event
    private final static String EVENT_TABLE = "tbl_event";
    private final static String SQL_SELECT_ALL_EVENT = "SELECT id, event_id, user_id, line_id, display_name FROM " + EVENT_TABLE;
    private final static String SQL_JOIN_EVENT = "INSERT INTO " + EVENT_TABLE + " (event_id, user_id, line_id, display_name) VALUES (?, ?, ?, ?);";
    private final static String SQL_GET_BY_EVENT_ID = SQL_SELECT_ALL_EVENT + " WHERE LOWER(event_id) LIKE LOWER(?);";
    private final static String SQL_GET_BY_JOIN = SQL_SELECT_ALL_EVENT + " WHERE event_id = ? AND user_id = ?;";

    private JdbcTemplate mJdbc;

    private final static ResultSetExtractor<User> SINGLE_RS_EXTRACTOR = new ResultSetExtractor() {
        @Override
        public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
            while (rs.next()) {
                User user = new User(
                        rs.getLong("id"),
                        rs.getString("user_id"),
                        rs.getString("line_id"),
                        rs.getString("display_name")
                );

                return user;
            }
            return null;
        }
    };

    private final static ResultSetExtractor<List<User>> MULTIPLE_RS_EXTRACTOR = new ResultSetExtractor<List<User>>() {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {

            List<User> list = new Vector<User>();

            while (rs.next()) {
                User user = new User(
                        rs.getLong("id"),
                        rs.getString("user_id"),
                        rs.getString("line_id"),
                        rs.getString("display_name")
                );
                list.add(user);
            }

            return list;
        }
    };

    private final static ResultSetExtractor<JointEvents> SINGLE_RS_EXTRACTOR_EVENT = new ResultSetExtractor<JointEvents>() {
        @Override
        public JointEvents extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            while (resultSet.next()) {
                JointEvents joinEvents = new JointEvents(
                        resultSet.getLong("id"),
                        resultSet.getString("event_id"),
                        resultSet.getString("user_id"),
                        resultSet.getString("line_id"),
                        resultSet.getString("display_name"));

                return joinEvents;
            }
            return null;
        }
    };

    private final static ResultSetExtractor<List<JointEvents>> MULTIPLE_RS_EXTRACTOR_EVENT = new ResultSetExtractor<List<JointEvents>>() {
        @Override
        public List<JointEvents> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            List<JointEvents> list = new Vector<>();
            while (resultSet.next()) {
                JointEvents jointEvents = new JointEvents(
                        resultSet.getLong("id"),
                        resultSet.getString("event_id"),
                        resultSet.getString("user_id"),
                        resultSet.getString("line_id"),
                        resultSet.getString("display_name"));
                list.add(jointEvents);
            }
            return list;
        }
    };

    public DaoImpl(DataSource dataSource) {
        mJdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public List<User> get() {
        return mJdbc.query(SQL_SELECT_ALL, MULTIPLE_RS_EXTRACTOR);
    }

    @Override
    public List<User> getByUserId(String aUserId) {
        return mJdbc.query(SQL_GET_BY_USER_ID, new Object[]{"%"+aUserId+"%"}, MULTIPLE_RS_EXTRACTOR);
    }

    @Override
    public int registerLineId(String aUserId, String aLineId, String aDisplayName) {
        return mJdbc.update(SQL_REGISTER, new Object[]{aUserId, aLineId, aDisplayName});
    }

    @Override
    public int joinEvent(String aEventId, String aUserId, String aLineId, String aDisplayName) {
        return mJdbc.update(SQL_JOIN_EVENT, new Object[]{aEventId, aUserId, aLineId, aDisplayName});
    }

    @Override
    public List<JointEvents> getEvent() {
        return mJdbc.query(SQL_SELECT_ALL_EVENT, MULTIPLE_RS_EXTRACTOR_EVENT);
    }

    @Override
    public List<JointEvents> getByEventId(String aEventId) {
        return mJdbc.query(SQL_GET_BY_EVENT_ID, new Object[]{"%"+aEventId+"%"}, MULTIPLE_RS_EXTRACTOR_EVENT);
    }

    @Override
    public List<JointEvents> getByJoin(String aEventId, String aUserId) {
        return mJdbc.query(SQL_GET_BY_JOIN, new Object[]{aEventId, aUserId}, MULTIPLE_RS_EXTRACTOR_EVENT);
    }
}
