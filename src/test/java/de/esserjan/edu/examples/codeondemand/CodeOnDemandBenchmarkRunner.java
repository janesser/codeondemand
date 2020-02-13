package de.esserjan.edu.examples.codeondemand;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

public class CodeOnDemandBenchmarkRunner {

    public static void main(String[] args) throws RunnerException {
        new Runner(
                new OptionsBuilder()
                        .include(CodeOnDemandDemoTest.class.getPackageName() + ".*")
                        .forks(2)
                        .warmupIterations(1)
                        .measurementIterations(1)
                        .measurementTime(TimeValue.minutes(1L))
                        .build()
        ).run();
    }
}
