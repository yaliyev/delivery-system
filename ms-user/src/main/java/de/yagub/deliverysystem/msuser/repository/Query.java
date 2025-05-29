package de.yagub.deliverysystem.msuser.repository;

public enum Query {

    SAVE("INSERT INTO users (id, username, password_hash, enabled) " +
            "VALUES (users_seq.NEXTVAL, ?, ?, ?)"),
    FIND_BY_USERNAME("SELECT * FROM users WHERE username = ?"),
    EXISTS_BY_USERNAME("SELECT COUNT(*) FROM users WHERE username = ?");

    private final String query;

    private Query(String query) {
      this.query = query;
    }

    public String getQuery(){
        return query;
    }
}
