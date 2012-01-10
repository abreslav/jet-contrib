CodeMirror.defineMode("kotlin", function (config, parserConfig) {
    function words(str) {
        var obj = {}, words = str.split(" ");
        for (var i = 0; i < words.length; ++i) obj[words[i]] = true;
        return obj;
    }

    var multiLineStrings = parserConfig.multiLineStrings;

    var keywords = words(
        "package continue return object while break class trait throw super" +
            " when type this else This try val var fun for is in if do as true false null get set");
    var softKeywords = words("import" +
        " where by get set abstract enum open annotation override private public internal" +
        " protected catch out vararg inline finally final ref");
    var blockKeywords = words("catch class do else finally for if where try while enum");
    var atoms = words("null true false this");

    var curPunc;

    function tokenBase(stream, state) {
        var ch = stream.next();
        if (ch == '"' || ch == "'") {
            return startString(ch, stream, state);
        }
        //for import sth.* (wo ;)
        if (ch == "." && stream.eat("*")) {
            return "word";
        }
        if (/[\[\]{}\(\),;\:\.]/.test(ch)) {
            curPunc = ch;
            return null
        }
        if (/\d/.test(ch)) {
//            stream.eatWhile(/[\w\.]/);
            if (stream.eat(/eE/)) {
                stream.eat(/\+\-/);
                stream.eatWhile(/\d/);
            }
            return "number";
        }
        if (ch == "/") {
            if (stream.eat("*")) {
                state.tokenize.push(tokenComment);
                return tokenComment(stream, state);
            }
            if (stream.eat("/")) {
                stream.skipToEnd();
                return "comment";
            }
            if (expectExpression(state.lastToken)) {
                return startString(ch, stream, state);
            }
        }
        /*if (ch == "-" && stream.eat(">")) {
         curPunc = "->";
         return null;
         }*/
        if (/[+\-*&%=<>!?|\/~]/.test(ch)) {
            stream.eatWhile(/[+\-*&%=<>|~]/);
            return "operator";
        }
        stream.eatWhile(/[\w\$_]/);
        /*if (ch == "@") {
         stream.eatWhile(/[\w\$_\.]/);
         return "meta";
         }*/
        /*if (state.lastToken == ".") return "property";
         if (stream.eat(":")) {
         curPunc = "proplabel";
         return "property";
         }*/
        var cur = stream.current();
        if (atoms.propertyIsEnumerable(cur)) {
            return "atom";
        }
        if (softKeywords.propertyIsEnumerable(cur)) {
            if (blockKeywords.propertyIsEnumerable(cur)) curPunc = "newstatement";
            return "softKeyword";
        }

        if (keywords.propertyIsEnumerable(cur)) {
            if (blockKeywords.propertyIsEnumerable(cur)) curPunc = "newstatement";
            return "keyword";
        }
        return "word";
    }

    tokenBase.isBase = true;

    function startString(quote, stream, state) {
        var tripleQuoted = false;
        if (quote != "/" && stream.eat(quote)) {
            if (stream.eat(quote)) tripleQuoted = true;
            else return "string";
        }
        function t(stream, state) {
            var escaped = false, next, end = !tripleQuoted;
//            stream.eatWhile(/\w,.!?/);
            while ((next = stream.next()) != null) {
                if (next == quote && !escaped) {
                    if (!tripleQuoted) {
                        break;
                    }
                    if (stream.match(quote + quote)) {
                        end = true;
                        break;
                    }
                }

                if (next == "$" && !escaped && stream.eat("{")) {
                    state.tokenize.push(tokenBaseUntilBrace());
                    return "string";
                }

                /*if (next == "$" && !escaped) {
                    state.tokenize.push(tokenBaseUntilSpace());
                    return "string";
                }*/
                /*if (quote == '"' && next == "$" && !escaped && stream.eat("{")) {
                 state.tokenize.push(tokenBaseUntilBrace());
                 return "string";
                 }*/
                escaped = !escaped && next == "\\";
            }
            if (multiLineStrings)
                state.tokenize.push(t);
            if (end) state.tokenize.pop();
            return "string";
        }

        state.tokenize.push(t);
        return t(stream, state);
    }

    function tokenBaseUntilBrace() {
        var depth = 1;

        function t(stream, state) {
            if (stream.peek() == "}") {
                depth--;
                if (depth == 0) {
                    state.tokenize.pop();
                    return state.tokenize[state.tokenize.length - 1](stream, state);
                }
            } else if (stream.peek() == "{") {
                depth++;
            }
            return tokenBase(stream, state);
        }

        t.isBase = true;
        return t;
    }

    function tokenBaseUntilSpace() {

        /*var depth = 1;

         function t(stream, state) {
         if (stream.peek() == " ") {
         alert(depth) ;
         depth--;
         if (depth == 0) {
         state.tokenize.pop();
         return state.tokenize[state.tokenize.length - 1](stream, state);
         }
         } */
        /*else if (stream.peek() == "$") {
         depth++;
         }*/
        /*
         return tokenBase(stream, state);
         }

         t.isBase = true;
         return t;*/
        var depth = 1;

        function t(stream, state) {
            stream.eatWhile(/[\w]/);
            //if (stream.peek() == " ") {
           //
                return tokenBase(stream, state);
           // }
//            state.tokenize.pop();
//            return state.tokenize[state.tokenize.length - 1](stream, state);

        }

        t.isBase = true;
        return t;
    }

    function tokenComment(stream, state) {
        var maybeEnd = false, ch;
        while (ch = stream.next()) {
            if (ch == "/" && maybeEnd) {
                state.tokenize.pop();
                break;
            }
            maybeEnd = (ch == "*");
        }
        return "comment";
    }

    function expectExpression(last) {
        var r = !last || last == "operator" || last == "->" || /[\.\[\{\(,;:]/.test(last) ||
            last == "newstatement" || last == "keyword" || last == "proplabel";
//        document.getElementById("debug").innerHTML += r + " " + last + " ";
        return r;
    }

    function Context(indented, column, type, align, prev) {
        this.indented = indented;
        this.column = column;
        this.type = type;
        this.align = align;
        this.prev = prev;
    }

    function pushContext(state, col, type) {
        return state.context = new Context(state.indented, col, type, null, state.context);
    }

    function popContext(state) {
        var t = state.context.type;
        if (t == ")" || t == "]" || t == "}")
            state.indented = state.context.indented;
        return state.context = state.context.prev;
    }

    // Interface

    return {
        startState:function (basecolumn) {
            return {
                tokenize:[tokenBase],
                context:new Context((basecolumn || 0) - config.indentUnit, 0, "top", false),
                indented:0,
                startOfLine:true,
                lastToken:null
            };
        },

        token:function (stream, state) {
            var ctx = state.context;
            if (stream.sol()) {
                if (ctx.align == null) ctx.align = false;
                state.indented = stream.indentation();
                state.startOfLine = true;
                // Automatic semicolon insertion
                if (ctx.type == "statement" && !expectExpression(state.lastToken)) {
                    popContext(state);
                    ctx = state.context;
                }
            }
            if (stream.eatSpace()) return null;
            curPunc = null;
            var style = state.tokenize[state.tokenize.length - 1](stream, state);
            if (style == "comment") return style;
            if (ctx.align == null) ctx.align = true;
            if ((curPunc == ";" || curPunc == ":") && ctx.type == "statement") popContext(state);
            // Handle indentation for {x -> \n ... }
            else if (curPunc == "->" && ctx.type == "statement" && ctx.prev.type == "}") {
                popContext(state);
                state.context.align = false;
            }
            else if (curPunc == "{") pushContext(state, stream.column(), "}");
            else if (curPunc == "[") pushContext(state, stream.column(), "]");
            else if (curPunc == "(") pushContext(state, stream.column(), ")");
            else if (curPunc == "}") {
                while (ctx.type == "statement") ctx = popContext(state);
                if (ctx.type == "}") ctx = popContext(state);
                while (ctx.type == "statement") ctx = popContext(state);
            }
            else if (curPunc == ctx.type) popContext(state);
            else if (ctx.type == "}" || ctx.type == "top" || (ctx.type == "statement" && curPunc == "newstatement"))
                pushContext(state, stream.column(), "statement");
            state.startOfLine = false;
            state.lastToken = curPunc || style;
            return style;
        },

        indent:function (state, textAfter) {
            if (!state.tokenize[state.tokenize.length - 1].isBase) return 0;
            var firstChar = textAfter && textAfter.charAt(0), ctx = state.context;
            if (ctx.type == "statement" && !expectExpression(state.lastToken)) ctx = ctx.prev;
            var closing = firstChar == ctx.type;
//            alert("last " + ctx.type + " first " + firstChar + " after " + textAfter);
            if (ctx.type == "statement") {
                return ctx.indented + (firstChar == "{" ? 0 : config.indentUnit);
            }
            else if (ctx.align) return ctx.column + (closing ? 0 : 1);
            else return ctx.indented + (closing ? 0 : config.indentUnit);
        },

        electricChars:"{}"
    };
});

CodeMirror.defineMIME("text/kotlin", "kotlin");
