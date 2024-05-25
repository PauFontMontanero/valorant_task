package com.valorant.dbtestutils.junit;

import java.sql.Connection;

public class DbAssertions {
    public static DbAssertion assertThat(Connection connection) {
        return new DbAssertion(connection);
    } 
}
