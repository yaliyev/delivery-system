package de.yagub.deliverysystem.mswallet.repository;

public enum Query {
    FIND_ALL("""
            SELECT * FROM (
                SELECT a.*, ROWNUM rnum FROM (
                    SELECT ID, USER_ID, BALANCE, CURRENCY,
                           CREATED_AT, UPDATED_AT, STATUS, VERSION
                    FROM WALLETS
                    ORDER BY ID
                ) a WHERE ROWNUM <= ?
            ) WHERE rnum > ?"""),
    UPDATE("""
                UPDATE WALLETS
                SET USER_ID = ?,
                    BALANCE = ?,
                    CURRENCY = ?,
                    STATUS = ?,
                    UPDATED_AT = ?,
                    VERSION = VERSION + 1
                WHERE ID = ? AND VERSION = ?
                """),
    FIND_BY_ID("SELECT * FROM WALLETS WHERE ID = ?"),
    FIND_BY_USER_ID("""
            SELECT ID, USER_ID, BALANCE, CURRENCY, 
                   CREATED_AT, UPDATED_AT, STATUS, VERSION
            FROM WALLETS
            WHERE USER_ID = ?"""),
    UPDATE_BALANCE("""
            UPDATE WALLETS 
            SET BALANCE = BALANCE + ?, 
                UPDATED_AT = SYSTIMESTAMP,
                VERSION = VERSION + 1
            WHERE ID = ?""");

    private final String query;

    private Query(String query){
        this.query = query;
    }

    public String getQuery(){
        return query;
    }
}
