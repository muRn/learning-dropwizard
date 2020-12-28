package com.github.murn;

import com.github.murn.db.JdbiFactory;
import com.github.murn.health.DatabaseHealthCheck;
import com.github.murn.health.TemplateHealthCheck;
import com.github.murn.resources.GreetingResource;
import com.github.murn.resources.PostResource;
import com.github.murn.resources.PostsResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class LearningDropwizardApplication extends Application<LearningDropwizardConfiguration> {
    public static void main(final String[] args) throws Exception {
        new LearningDropwizardApplication().run(args);
    }

    @Override
    public String getName() {
        return "LearningDropwizard";
    }

    @Override
    public void initialize(final Bootstrap<LearningDropwizardConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<LearningDropwizardConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(LearningDropwizardConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final LearningDropwizardConfiguration configuration,
                    final Environment environment) {
        // initialize database connection pool
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(configuration.getDataSourceFactory());
        jdbi.installPlugin(new SqlObjectPlugin());

        // add all resources
        final GreetingResource resource = new GreetingResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(resource);
        environment.jersey().register(new PostResource(jdbi));
        environment.jersey().register(new PostsResource(jdbi));

        // add health checks
        final TemplateHealthCheck tmplHealthCheck = new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", tmplHealthCheck);
        final DatabaseHealthCheck dbHealthCheck = new DatabaseHealthCheck(jdbi);
        environment.healthChecks().register("database", dbHealthCheck);
    }
}
