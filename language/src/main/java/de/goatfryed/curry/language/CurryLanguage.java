package de.goatfryed.curry.language;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.debug.DebuggerTags;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.interop.InteropLibrary;
import de.goatfryed.curry.language.ast.CurryProgramRootNode;
import de.goatfryed.curry.language.parser.CurryLanguageParser;

@TruffleLanguage.Registration(
    id = CurryLanguage.ID,
    name = "Curry",
    defaultMimeType = CurryLanguage.MIME_TYPE,
    characterMimeTypes = CurryLanguage.MIME_TYPE,
    fileTypeDetectors = CurryFileDetector.class
)
@ProvidedTags({StandardTags.CallTag.class, StandardTags.StatementTag.class, StandardTags.RootTag.class, StandardTags.RootBodyTag.class, StandardTags.ExpressionTag.class, DebuggerTags.AlwaysHalt.class,
    StandardTags.ReadVariableTag.class, StandardTags.WriteVariableTag.class})
public class CurryLanguage extends TruffleLanguage<CurryContext> {
    public static final String ID = "curry";
    public static final String DEFAULT_EXTENSION = "." + ID;
    public static final String MIME_TYPE = "application/x-tapa";

    public CurryLanguage() {
    }

    @Override
    protected CallTarget parse(ParsingRequest request) throws Exception {

        var curryContext = new CurryLanguageParser(this).parseCurry(request.getSource());

        var mainCall = new CurryProgramRootNode(this, curryContext.main);

        return Truffle.getRuntime()
            .createCallTarget(mainCall);
    }

    @Override
    protected CurryContext createContext(Env env) {
        return new CurryContext(env);
    }

    @Override
    protected boolean isVisible(CurryContext context, Object value) {
        return !InteropLibrary.getFactory().getUncached(value).isNull(value);
    }
}
