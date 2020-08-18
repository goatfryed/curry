package de.goatfryed.curry.language;

import com.oracle.truffle.api.TruffleLanguage;

import java.io.PrintWriter;

public class CurryContext {

    private final TruffleLanguage.Env env;
    private final PrintWriter output;

    public CurryContext(TruffleLanguage.Env env) {

        this.env = env;

        this.output = new PrintWriter(env.out(), true);
    }

    public TruffleLanguage.Env getEnv() {
        return env;
    }

    public PrintWriter getOutput() {
        return output;
    }
}
