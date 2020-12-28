package com.github.murn.health;

import com.codahale.metrics.health.HealthCheck;
import org.jdbi.v3.core.Jdbi;

public class DatabaseHealthCheck extends HealthCheck {
    private final Jdbi jdbi;

    public DatabaseHealthCheck(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    protected Result check() throws Exception {
        Integer result = jdbi.withHandle(handle ->
                handle.createQuery("select 1")
                .mapTo(Integer.class)
                .one());
        if (result != 1) {
            return Result.unhealthy("Db returned " + result + " when asked for 1");
        }

        return Result.healthy();
    }
}
