package de.esserjan.edu.examples.codeondemand;

import com.github.javafaker.Faker;
import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.Before;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.*;

import javax.script.ScriptException;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

/**
 * considerations of scopes
 * - one scope having validation compiled
 * - one scope validating with a certain binding
 * <p>
 * how does thread-safety apply?
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
public class CodeOnDemandDemoTest {

    public static final String JS_VALIDATE_FUNCTION = "(function(param) { return param ? true : false; })";

    private NashornSandbox jsSandbox;

    private Value polyglotValidate;

    private Faker faker = new Faker();

    @Setup
    @Before
    public void initJsSandbox() throws ScriptException {
        jsSandbox = NashornSandboxes.create();

        /*
         * disallow everything
         *
         * see delight.nashornsandbox.internal.NashornSandboxImpl.produceSecureBindings
         *
         * e.g. this generates empty stubs instead of the real functions.
         */

        jsSandbox.disallowAllClasses();
        jsSandbox.allowGlobalsObjects(false);
        jsSandbox.allowLoadFunctions(false);
        jsSandbox.allowNoBraces(false);
        jsSandbox.allowPrintFunctions(false);
        jsSandbox.allowExitFunctions(false);

        // TODO reconsider multi-threading here
        jsSandbox.setExecutor(Executors.newCachedThreadPool());

        // read from API javascript code, once in a while
        jsSandbox.eval("var validate = " + JS_VALIDATE_FUNCTION.substring(1, JS_VALIDATE_FUNCTION.length() - 1) + ";");
    }

    @Setup
    @Before
    public void initPolyglotContext() {
        Context ctx = Context.create();
        polyglotValidate = ctx.eval("js", JS_VALIDATE_FUNCTION);
    }

    @Benchmark
    @Test
    public void testNashornInvocableValidation() throws ScriptException, NoSuchMethodException {
        boolean resultTrue = (boolean) jsSandbox.getSandboxedInvocable().invokeFunction("validate", faker.lorem().word());
        assertTrue(resultTrue);

        boolean resultFalse = (boolean) jsSandbox.getSandboxedInvocable().invokeFunction("validate", "");
        assertFalse(resultFalse);
    }

    @Benchmark
    @Test
    public void testPolyglotValidation() {
        Value resultTrue = polyglotValidate.execute(faker.lorem().word());
        assertTrue(resultTrue.asBoolean());

        Value resultFalse = polyglotValidate.execute("");
        assertFalse(resultFalse.asBoolean());
    }

}
