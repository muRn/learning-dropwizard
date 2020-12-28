package com.github.murn;

import com.github.murn.db.JdbiFactory;
import com.github.murn.health.TemplateHealthCheck;
import com.github.murn.resources.GreetingResource;
import com.github.murn.resources.PostResource;
import io.dropwizard.Application;
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
        // TODO: application initialization
    }

    @Override
    public void run(final LearningDropwizardConfiguration configuration,
                    final Environment environment) {
        final GreetingResource resource = new GreetingResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(resource);
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(configuration.getDataSourceFactory());
        jdbi.installPlugin(new SqlObjectPlugin());
        environment.jersey().register(new PostResource(jdbi));

        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
    }

}
