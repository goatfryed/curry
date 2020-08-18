package de.goatfryed.curry.language.parser;

import com.oracle.truffle.api.source.Source;
import de.goatfryed.curry.language.CurryLanguage;
import de.goatfryed.curry.language.ast.CurryProgramRootNode;
import de.goatfryed.curry.language.parser.CurryParser;
import de.goatfryed.curry.language.parser.CurryLexer;
import org.antlr.v4.runtime.*;

import java.util.ArrayList;

public class CurryLanguageParser {

    private final CurryLanguage language;

    public CurryLanguageParser(CurryLanguage language) {

        this.language = language;
    }

    public CurryParser.CurryContext parseCurry(Source source) {

        var lexer = new CurryLexer(CharStreams.fromString(source.getCharacters().toString()));
        var parser = new CurryParser(new CommonTokenStream(lexer));
        parser.setNodeFactory(new Builder(language));

        var errors = new ArrayList<ParserError>();

        parser.addErrorListener(
            new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                    var column = charPositionInLine + 1;
                    var token = (Token) offendingSymbol;
                    var length = token == null ? 1 : Math.max(0, token.getStopIndex() - token.getStartIndex());
                    var errorMsg = String.format(
                        "Error(s) parsing script:%n\t-- line %d col %d\t-> %s",
                        line, column, msg
                    );
                    var error = new ParserError(source, line, column, length, errorMsg);
                    errors.add(error);
                }
            }
        );

        var context = parser.curry();

        if (errors.size() > 0) {
            throw errors.get(0);
        }

        return context;
    }
}
