package de.yagub.deliverysystem.msorder.repository;

public enum Query {

    UPDATE("UPDATE ORDERS SET CUSTOMER_ID = ?, TOTAL_AMOUNT = ?, STATUS = ?, UPDATED_AT = ? WHERE ID = ?"),
    DELETE_ORDER_ITEM("DELETE FROM ORDER_ITEMS WHERE ORDER_ID = ?"),
    INSERT_ORDER_ITEM("INSERT INTO ORDER_ITEMS (ORDER_ID, PRODUCT_ID, QUANTITY, PRICE_PER_UNIT) VALUES (?, ?, ?, ?)"),
    FIND_BY_ID("SELECT * FROM ORDERS WHERE ID = ?"),
    FIND_BY_CUSTOMER_ID("SELECT * FROM ORDERS WHERE CUSTOMER_ID = ?"),
    FIND_ALL("SELECT * FROM ORDERS"),
    FIND_ALL_ORDER_ITEMS("SELECT * FROM ORDER_ITEMS WHERE ORDER_ID = ?");


    private final String query;

    Query(String query) {
        this.query = query;
    }

    public String getQuery(){
        return query;
    }
}
