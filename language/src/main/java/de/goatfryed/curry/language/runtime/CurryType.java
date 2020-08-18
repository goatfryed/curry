package de.goatfryed.curry.language.runtime;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import de.goatfryed.curry.language.CurryLanguage;

@ExportLibrary(InteropLibrary.class)
public class CurryType implements TruffleObject {

    public static final CurryType NULL = new CurryType("NULL", (l, v) -> l.isNull(v));
    private final String name;
    private final TypeCheck isInstance;

    private CurryType(String name, TypeCheck isInstance) {
        this.name = name;
        this.isInstance = isInstance;
    }


    /**
     * Checks whether this type is of a certain instance. If used on fast-paths it is required to
     * cast {@link CurryType} to a constant.
     */
    public boolean isInstance(Object value, InteropLibrary interop) {
        CompilerAsserts.partialEvaluationConstant(this);
        return isInstance.check(interop, value);
    }

    @ExportMessage
    boolean hasLanguage() {
        return true;
    }

    @ExportMessage
    Class<? extends TruffleLanguage<?>> getLanguage() {
        return CurryLanguage.class;
    }

    /*
     * All SLTypes are declared as interop meta-objects. Other example for meta-objects are Java
     * classes, or JavaScript prototypes.
     */
    @ExportMessage
    boolean isMetaObject() {
        return true;
    }

    /*
     * SL does not have the notion of a qualified or simple name, so we return the same type name
     * for both.
     */
    @ExportMessage(name = "getMetaQualifiedName")
    @ExportMessage(name = "getMetaSimpleName")
    public Object getName() {
        return name;
    }

    @ExportMessage(name = "toDisplayString")
    Object toDisplayString(@SuppressWarnings("unused") boolean allowSideEffects) {
        return name;
    }

    @Override
    public String toString() {
        return "TapareaType[" + name + "]";
    }

    /*
     * The interop message isMetaInstance might be used from other languages or by the {@link
     * SLIsInstanceBuiltin isInstance} builtin. It checks whether a given value, which might be a
     * primitive, foreign or SL value is of a given SL type. This allows other languages to make
     * their instanceOf interopable with foreign values.
     */
    @ExportMessage
    static class IsMetaInstance {

        /*
         * We assume that the same type is checked at a source location. Therefore we use an inline
         * cache to specialize for observed types to be constant. The limit of "3" specifies that we
         * specialize for 3 different types until we rewrite to the doGeneric case. The limit in
         * this example is somewhat arbitrary and should be determined using careful tuning with
         * real world benchmarks.
         */
        @Specialization(guards = "type == cachedType", limit = "3")
        static boolean doCached(@SuppressWarnings("unused") CurryType type, Object value,
                                @Cached("type") CurryType cachedType,
                                @CachedLibrary("value") InteropLibrary valueLib) {
            return cachedType.isInstance.check(valueLib, value);
        }

        @CompilerDirectives.TruffleBoundary
        @Specialization(replaces = "doCached")
        static boolean doGeneric(CurryType type, Object value) {
            return type.isInstance.check(InteropLibrary.getFactory().getUncached(), value);
        }
    }

    @FunctionalInterface
    interface TypeCheck {

        boolean check(InteropLibrary lib, Object value);

    }
}
