package org.servlets.dao;

public abstract class BaseDAO {
    protected final static String DB_URL = System.getenv("SERVLETS_DB_URL");
    protected final static String DB_USER_NAME = System.getenv("SERVLETS_DB_USER_NAME");
    protected final static String DB_PASSWORD = System.getenv("SERVLETS_DB_PASSWORD");
}
