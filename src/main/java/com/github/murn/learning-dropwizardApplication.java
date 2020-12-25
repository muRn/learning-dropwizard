package com.github.murn;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class learning-dropwizardApplication extends Application<learning-dropwizardConfiguration> {

    public static void main(final String[] args) throws Exception {
        new learning-dropwizardApplication().run(args);
    }

    @Override
    public String getName() {
        return "learning-dropwizard";
    }

    @Override
    public void initialize(final Bootstrap<learning-dropwizardConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final learning-dropwizardConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
