package com.github.murn.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.dropwizard.db.DataSourceFactory;
import org.jdbi.v3.core.Jdbi;

public class JdbiFactory {
    public Jdbi build(DataSourceFactory dataSourceFactory) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dataSourceFactory.getUrl());
//        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.setUsername(dataSourceFactory.getUser());
        config.setPassword(dataSourceFactory.getPassword());
        HikariDataSource ds = new HikariDataSource(config);
        return Jdbi.create(ds);
    }
}
