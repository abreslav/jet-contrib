var arrayClasses = [];
var arrayLinesMarkers = [];

function removeStyles() {
    var i = 0;
    while (typeof arrayClasses[i] != "undefined") {
        arrayClasses[i].clear();
        i++;
    }
    i = 0;
    while (typeof arrayLinesMarkers[i] != "undefined") {
        editor.clearMarker(arrayLinesMarkers[i]);
        i++;
    }
}

$(document).ready(function () {

    var goToSymbolShortcutKeys = [17, 32];
    var runShortcutKeys = [17, 120];

    var timer;
    var timerIntervalForNonPrinting = 300;

    var isCompletionInProgress = false;
    var isCompilationInProgress = false;
    var isLoadingHighlighting = false;

    var keywords;
    var isContinueComplete = false;

    editor = CodeMirror.fromTextArea(document.getElementById("code"), {
        lineNumbers:true,
        matchBrackets:true,
        mode:"text/kotlin",
        onKeyEvent:function (i, event) {
            // Hook into ctrl-space
            if (isGotoKeysPressed(event, goToSymbolShortcutKeys)) {
                if (event.preventDefault) event.preventDefault();
                else event.returnValue = false;
                event.stop();
                beforeComplete();
                return true;
            } else if (isGotoKeysPressed(event, runShortcutKeys)) {
                if (event.preventDefault) event.preventDefault();
                else event.returnValue = false;
                event.stop();
                runOrCompile("run", "Run project...", "Run action failed.");
                return true;
            }

        },
        onChange:runTimerForNonPrinting,
        onCursorActivity:updateStatusBar,
        minHeight:"430px"
    });

    function runTimerForNonPrinting() {
        window.onbeforeunload = function () {
            return BEFORE_EXIT;
        };
        if (timer) {
            clearTimeout(timer);
            timer = setTimeout(getHighlighting, timerIntervalForNonPrinting);
        }
        else {
            timer = setTimeout(getHighlighting, timerIntervalForNonPrinting);
        }
    }

    function getHighlighting() {
        $("#tabs").tabs("select", 0);
        if (!isCompletionInProgress && !loadingExample && !isLoadingHighlighting) {
            getErrors();
        }
    }

    function getErrors() {
        isLoadingHighlighting = true;
        var i = editor.getValue();
        if (isApplet) {
            try {
                var dataFromApplet;
                try {
                    dataFromApplet = $("#myapplet")[0].getHighlighting(i);
                } catch (e) {
                    document.getElementById("debug").innerHTML = e.description;
                    $(".applet-disable").click();
                    getErrors();
                    isLoadingHighlighting = false;
                    return;
                }
                var data = eval(dataFromApplet);
                if (typeof data != "undefined") {
                    if ((typeof data[0] != "undefined") && (typeof data[0].exception != "undefined")) {
                        $("#tabs").tabs("select", 0);
                        document.getElementById("problems").innerHTML = "";
                        setStatusBarMessage(data[0].exception);
                        setConsoleMessage(data[0].exception);
                        var j = 0;
                        while (typeof data[j] != "undefined") {
                            exception(data[j]);
                            j++;
                        }
                        return;
                    } else {
                        onHighlightingSuccess(data);
                    }
                }
                isLoadingHighlighting = false;
            } catch (e) {
                document.getElementById("debug").innerHTML = e.description;
                isLoadingHighlighting = false;
            }
        } else {
            $.ajax({
                //url: document.location.href + "?sendData=true&" + new Date().getTime() + "&lineNumber=" + lineNumber,
                url:document.location.href + "?sessionId=" + sessionId + "&sendData=true",
                context:document.body,
                success:onHighlightingSuccess,
                dataType:"json",
                type:"POST",
                data:{text:i},
                timeout:10000,
                error:function () {
                    isLoadingHighlighting = false;
                    setConsoleMessage("Ajax request for highlighting was aborted.");
                }
            });
        }
    }

    function onHighlightingSuccess(data) {
        if (data != null) {
            removeStyles();
            if ((typeof data[0] != "undefined") && (typeof data[0].exception != "undefined")) {
                document.getElementById("problems").innerHTML = "";
                setStatusBarMessage(data[0].exception);
                setConsoleMessage(data[0].exception);
                var j = 0;
                while (typeof data[j] != "undefined") {
                    exception(data[j]);
                    j++;
                }
                //                updateStatusBar();
                isLoadingHighlighting = false;
                return;
            }
            var i = 0;

            var problems = document.createElement("div");
            while (typeof data[i] != "undefined") {
                arrayClasses.push(editor.markText(eval('(' + data[i].x + ')'), eval('(' + data[i].y + ')'), data[i].className, "ddd"));
                var title = data[i].titleName;
                var start = eval('(' + data[i].x + ')');
                var severity = data[i].severity;

                if ((editor.lineInfo(start.line) != null) && (editor.lineInfo(start.line).markerText == null)) {
                    if ((data[i].severity == 'WARNING') || (data[i].severity == 'TYPO')) {
                        //editor.setMarker(start.line, '<img src="/icons/warning.png" title="' + title + '"/>%N%');
                        editor.setMarker(start.line, '<span class=\"warningGutter\" title="' + title + '">  </span>%N%');
                    } else {
                        editor.setMarker(start.line, '<span class=\"errorGutter\" title="' + title + '">  </span>%N%');
                    }
                    arrayLinesMarkers.push(start.line);
                } else {
                    var text = editor.lineInfo(start.line).markerText;
                    text = text.substring(text.indexOf("title=\"") + 7);
                    text = text.substring(0, text.indexOf("\""));
                    if ((severity == 'WARNING')) {
                        editor.setMarker(start.line, '<span class=\"warningGutter\" title="' + title + " ---next error--- " + text + '">  </span>%N%');
                    } else {
                        editor.setMarker(start.line, '<span class=\"errorGutter\" title="' + title + " ---next error--- " + text + '">  </span>%N%');
                    }
                    //arrayLinesMarkers.push(start.line);
                }

                setTimeout(function () {
                    var el = document.getElementById(start.line + " " + start.ch);
                    if (el != null) {
                        el.setAttribute("title", title);
                    }
                }, 50);

                //add exception at problemsView
                var p = createElementForProblemView(severity, start, title);
                problems.appendChild(p);


                i++;
            }
            document.getElementById("problems").innerHTML = problems.innerHTML;
        }
        updateStatusBar();
        isLoadingHighlighting = false;
    }

    function updateStatusBar() {
        updateHelp(editor.getTokenAt(editor.getCursor(true)).string);
        var lineNumber = editor.getCursor(true).line;
        var text = editor.lineInfo(lineNumber).markerText;
        if (text != null) {
            text = text.substring(text.indexOf("title=\"") + 7);
            text = text.substring(0, text.indexOf("\""));
            if (text.length > 90) text = text.substring(0, 90) + "...";
            setStatusBarMessage("line " + (lineNumber + 1) + " - " + text);
        } else {
            setStatusBarMessage("");
        }
    }

    function isGotoKeysPressed(event, array) {
        var args = args || {};

        for (var i = 0; i < array.length; ++i) {
            args[i] = array[i];
            if ((event.ctrlKey) && (args[i] == 17)) {
                args[i] = true;
            }
            if (args[i] == event.keyCode) {
                args[i] = true;
            }
        }
        for (var k = 0; k < array.length; ++k) {
            if (args[k] != true) {
                return false;
            }
        }
        return true;
    }

    $("#arguments").val("");

    function checkIfThereAreErrorsInProblemView() {
        var childrens = document.getElementById("problems").childNodes;
        var result = false;
        if (childrens.length > 0) {
            for (var i = 0; i < childrens.length; ++i) {
                if (childrens[i].className == "problemsViewError") {
                    result = true;
                    break;
                }
            }
            if (result) {
                setStatusBarMessage("During program execution errors have occurred.");
                document.getElementById("console").innerHTML = "See Problems View tab, there are errors in your code.";
            }
        }
        return result;
    }

    $("#run").click(function () {
        $("#tabs").tabs("select", 1);
        setStatusBarMessage("Running...");
        if ((!checkIfThereAreErrorsInProblemView()) && !isCompilationInProgress) {
            isCompilationInProgress = true;
            var arguments = $("#arguments").val();
            var i = editor.getValue();
            $.ajax({
                url:document.location.href + "?sessionId=" + sessionId + "&" + param + "=true",
                context:document.body,
                success:onCompileSuccess,
                dataType:"json",
                type:"POST",
                data:{text:i, consoleArgs:arguments},
                timeout:10000,
                error:function () {
                    isCompilationInProgress = false;
                    setStatusBarMessage("Running request was aborted.");
                    document.getElementById("console").innerHTML = "Your request is aborted. Impossible to get data from server. Internal server error.";
                }
            });
        }
    });

    function onCompileSuccess(data) {
        var isCompiledWithErrors = false;
        isCompilationInProgress = false;
        if (data != null) {
            if ((typeof data[0] != "undefined") && (typeof data[0].exception != "undefined")) {
                $("#tabs").tabs("select", 0);
                document.getElementById("problems").innerHTML = "";
                setStatusBarMessage(data[0].exception);
                setConsoleMessage(data[0].exception);
                var j = 0;
                while (typeof data[j] != "undefined") {
                    exception(data[j]);
                    j++;
                }
                //                updateStatusBar();
                isLoadingHighlighting = false;
                return;
            }
            var i = 0;
            setConsoleMessage("");
            var errors = document.createElement("div");
            while (typeof data[i] != "undefined") {
                //If there is a compilation error
                if (typeof data[i].message != "undefined") {
                    getErrors();
                    isCompiledWithErrors = true;
                    var pEr = document.createElement("p");
                    var image = document.createElement("img");
                    if (data[i].type == "ERROR") {
                        pEr.className = "problemsViewError";
                        image.src = "icons/error.png";
                    } else if (data[i].type == "WARNING") {
                        pEr.className = "problemsViewWarning";
                        image.src = "icons/warning.png";
                    }
                    pEr.appendChild(image);
                    var text = document.createElement("span");
                    text.innerHTML = data[i].message;
                    pEr.appendChild(text);
                    errors.appendChild(p);
                } else {
                    var p = document.createElement("p");
                    if ((data[i].type == "err") && (data[i].text != "")) {
                        p.className = "consoleViewError";
                        isCompiledWithErrors = true;
                    }
                    if (data[i].type == "info") {
                        p.className = "consoleViewInfo";
                    }
                    p.innerHTML = data[i].text;
                    errors.appendChild(p);
                }
                i++;
            }
            document.getElementById("console").appendChild(errors);
        }
        if (!isCompiledWithErrors) {
            setStatusBarMessage("Compilation competed without errors.");
        } else {
            setStatusBarMessage("During program execution errors have occurred");
        }
    }


    function createElementForProblemView(severity, start, title) {
        var p = document.createElement("p");
        var img = document.createElement("img");
        if (severity == 'WARNING') {
            img.src = "/icons/warning.png";
            p.className = "problemsViewWarning";
        } else if (severity == 'STACKTRACE') {
            p.className = "problemsViewStacktrace";
        } else {
            img.src = "/icons/error.png";
            p.className = "problemsViewError";
        }
        p.appendChild(img);
        var titleDiv = document.createElement("span");
        if (start == null) {
            titleDiv.innerHTML = " " + title;
        } else {

            titleDiv.innerHTML = "(" + (start.line + 1) + ", " + (start.ch + 1) + ") : " + title;
        }
        p.appendChild(titleDiv);
        return p;
    }

    function exception(ex) {
        var statusMes = "";
        var pos = ex.exception.indexOf("<br/>");
        if (pos != -1) {
            statusMes = ex.exception.substr(0, pos);
        }

        var problems = document.createElement("div");
        if (ex.type == "out") {
            document.getElementById("problems").appendChild(createElementForProblemView("STACKTRACE", null, ex.exception));
        } else {
            document.getElementById("problems").appendChild(createElementForProblemView("ERROR", null, ex.exception));
        }
    }

    function beforeComplete() {
        runTimerForNonPrinting();
        if (!isCompletionInProgress) {
            isCompletionInProgress = true;
            var i = editor.getValue();
            if (isApplet) {
                try {
                    var dataFromApplet;
                    try {
                        dataFromApplet = $("#myapplet")[0].getCompletion(i, editor.getCursor(true).line, editor.getCursor(true).ch);
                    } catch (e) {
                        document.getElementById("debug").innerHTML = e.description;
                        $(".applet-disable").click();
                        setStatusBarMessage("Applet doesn't supported on you computer.");
                        getErrors();
                        isCompletionInProgress = false;
                        return;
                    }
                    var data = eval(dataFromApplet);
                    if ((typeof data != "undefined") && (typeof data[0] != "undefined")) {
                        if (typeof data[0].exception != "undefined") {
                            $("#tabs").tabs("select", 0);
                            document.getElementById("problems").innerHTML = "";
                            setStatusBarMessage(data[0].exception);
                            setConsoleMessage(data[0].exception);
                            var j = 0;
                            while (typeof data[j] != "undefined") {
                                exception(data[j]);
                                j++;
                            }
                        } else {
                            startComplete(data);
                        }
                    }
                    isCompletionInProgress = false;
                } catch (e) {
                    document.getElementById("problems").innerHTML = e + " " + e.description;
                    isCompletionInProgress = false;
                }
            } else {
                $.ajax({
                    //url: document.location.href + "?sendData=true&" + new Date().getTime() + "&lineNumber=" + lineNumber,
                    url:document.location.href + "?sessionId=" + sessionId + "&complete=true&cursorAt=" + editor.getCursor(true).line + "," + editor.getCursor(true).ch,
                    context:document.body,
                    success:startComplete,
                    dataType:"json",
                    type:"POST",
                    data:{text:i},
                    timeout:10000,
                    error:function () {
                        isCompletionInProgress = false;
                        setConsoleMessage("Ajax request for completion was aborted.");
                    }
                });
            }
        }
    }

    function LookupElement(name, tail, icon) {
        this.icon = icon;
        this.name = name;
        this.tail = tail;
    }

    function continueComplete() {
        startComplete(null);
        isContinueComplete = true;
    }

    function startComplete(data) {
        isCompletionInProgress = false;
        //ideaKeywords = (data[0].content).split(" ");
        // We want a single cursor position.
        if (editor.somethingSelected()) return;
        // Find the token at the cursor
        var cur = editor.getCursor(), token = editor.getTokenAt(cur);
        if ((data != null) && (typeof data != "undefined")) {
            if ((typeof data[0] != "undefined") && (typeof data[0].exception != "undefined")) {
                setStatusBarMessage(data[0].exception);
                return;
            }
            keywords = [];
            var i = 0;
            while (typeof data[i] != "undefined") {
                var lookupElement = new LookupElement(data[i].name, data[i].tail, data[i].icon)
                keywords.push(lookupElement);
                i++;
            }
        } else {
            if (!isContinueComplete) {
                keywords = [];
                return;
            }
            isContinueComplete = false;
        }
        var completions = getCompletions(token);
        if ((completions.length == 0) || (completions == null)) return;
        if (completions.length == 1) {
            insert(completions[0].name);
            return;
        }


        function insert(str) {
            if (typeof str != "undefined") {
                var position = str.indexOf("(");
                if (position != "undefined") {
                    if (position != -1) {
                        //If this is a string with a package after
                        if (str.charAt(position - 1) == ' ') {
                            position = position - 2;
                        }
                        //if this is a method without args
                        if (str.charAt(position + 1) == ')') {
                            position++;
                        }
                        str = str.substring(0, position + 1);
                    }
                }
                position = str.indexOf(":");
                if (position != "undefined") {
                    if (position != -1) {
                        str = str.substring(0, position - 1);
                    }
                }
                if ((token.string == '.') || (token.string == ' ') || (token.string == '(')) {
                    editor.replaceRange(str, {line:cur.line, ch:token.end}, {line:cur.line, ch:token.end});
                } else {
                    editor.replaceRange(str, {line:cur.line, ch:token.start}, {line:cur.line, ch:token.end});
                }
            }
        }

        // Build the select widget
        var complete = document.createElement("div");
        complete.id = "complete";
        complete.className = "completions";

        var sel = document.createElement("select");
        sel.id = "selectId";
        for (i = 0; i < completions.length; ++i) {
            var opt = document.createElement("option");

//            opt.label = "<div><span style='background:yellow;display:inline-block;*display:inline;height:10px;width:10px;vertical-align:middle;zoom:1;'></span>" + completions[i].name +  " line 1</div><div>line 2</div><div>line 3</div>";
//            opt.innerHTML = "aaaaaaaaaaaaaaaaa";
            //            opt.title = "<div><span style=\"background:red;display:inline-block;*display:inline;height:10px;width:10px;vertical-align:middle;zoom:1;\"></span>" + completions[i].name + "</div>";
            //            opt.label = "<strong>ttt</strong><address>" + completions[i].name + "</address>";
            //            opt.title = "ssss";
            //            opt.innerHTML = "askjdsakjdh" + completions[i].name;
            //opt.backgroundImage = "url(/images/icon-ok.gif)";
            //            var aa = document.createElement("a");
            //             aa.innerHTML = "aaa";
            var pEl = document.createElement("p");
            pEl.className = "lookupElement";

            var icon = document.createElement("img");
            icon.className = "lookupElementIcon";
            icon.src = completions[i].icon;
            pEl.appendChild(icon);

            //opt.appendChild(document.createTextNode(completions[i].name));
            var spanName = document.createElement("div");
            spanName.className = "lookupElementName";
            spanName.innerHTML = completions[i].name;
            pEl.appendChild(spanName);

            var spanTail = document.createElement("div");
            spanTail.className = "lookupElementTail";
            spanTail.innerHTML = completions[i].tail;
            pEl.appendChild(spanTail);
            //opt.appendChild(document.createTextNode(completions[i].tail));
            opt.appendChild(pEl);
            //opt.label = opt.innerHTML;
            if (i == 0) {
                opt.id = "selected";
            }
//            opt.innerHTML = "<a>aaa</a>";
            sel.appendChild(opt);

            //opt.appendChild(aa);
        }

        complete.appendChild(sel);

        /*i = 0;
         while (typeof data[i] != "undefined") {
         var opt = sel.appendChild(document.createElement("option"));
         var image = document.createElement("img");
         image.src = data[i].icon;
         opt.appendChild(image);
         opt.appendChild(document.createTextNode(data[i].name));
         opt.appendChild(document.createTextNode(data[i].tail));
         i++;
         }*/

        sel.multiple = true;
        sel.firstChild.selected = true;
        sel.size = Math.min(10, i);

        var pos = editor.cursorCoords();
        complete.style.left = pos.x + "px";
        complete.style.top = pos.yBot + "px";
        document.body.appendChild(complete);
        document.getElementById("complete").focus();
        /*$("#selectId").sb({
         optionFormat:function () {
         alert($(this).attr("title"));
         return $(this).attr("alt");
         }
         });*/

        // Hack to hide the scrollbar.
        if (i <= 10)
            complete.style.width = (sel.clientWidth - 1) + "px";

        var done = false;

        function close() {
            if (done) return;
            done = true;
            complete.parentNode.removeChild(complete);
        }

        function pick() {
            var text = sel.options[sel.selectedIndex].childNodes[0].childNodes[1].textContent;
            if (typeof text == "undefined") {
                text = sel.options[sel.selectedIndex].childNodes[0].childNodes[1].innerHTML;
            }
            insert(text);
            //insert(sel.options[sel.selectedIndex].text);
            close();
            setTimeout(function () {
                editor.focus();
            }, 50);
        }

        connect(sel, "blur", close);
        connect(sel, "keydown", function (event) {
            var code = event.keyCode;

            // Enter and space
            if (code == 13 || code == 32) {
                event.stop();
                pick();
            }
            // Escape
            else if (code == 27) {
                event.stop();
                close();
                editor.focus();
            }
            else if (code != 38 && code != 40) {
                close();
                editor.focus();
                setTimeout(continueComplete, 50);
            }
        });


        connect(sel, "dblclick", pick);

        sel.focus();
        // Opera sometimes ignores focusing a freshly created node
        if (window.opera) setTimeout(function () {
            if (!done) sel.focus();
        }, 100);
        return true;
    }

    function createDropDown() {
        var source = $("#selectId");
        var selected = source.find("option[selected]");  // get selected <option>
        var options = $("option", source);  // get all <option> elements
        // create <dl> and <dt> with selected value inside it
        $("body").append('<dl id="target" class="dropdown"></dl>')
        $("#target").append('<dt><a href="#">' + selected.text() +
            '<span class="value">' + selected.val() +
            '</span></a></dt>')
        $("#target").append('<dd><ul></ul></dd>')
        // iterate through all the <option> elements and create UL
        options.each(function () {
            $("#target dd ul").append('<li><a href="#">' +
                $(this).text() + '<span class="value">' +
                $(this).val() + '</span></a></li>');
        });
    }


    function getCompletions(token) {
        var found = [], start = token.string;

        function maybeAdd(lookupElement) {
            if (lookupElement.name.indexOf(start) == 0) found.push(lookupElement);
        }

        function add(str) {
            found.push(str);
        }

        if (typeof keywords == "undefined") return found;
        if ((start.indexOf(' ') == 0) || (start == '.')) {
            forEachInArray(keywords, add);
        } else {
            forEachInArray(keywords, maybeAdd);
        }

        return found;
    }

    String.prototype.hashCode = function () {
        var hash = 0;
        if (this.length == 0) return hash;
        for (i = 0; i < this.length; i++) {
            char = this.charCodeAt(i);
            hash = ((hash << 5) - hash) + char;
            hash = hash & hash; // Convert to 32bit integer
        }
        return hash;
    }

    // Minimal event-handling wrapper.
    function stopEvent() {
        if (this.preventDefault) {
            this.preventDefault();
            this.stopPropagation();
        }
        else {
            this.returnValue = false;
            this.cancelBubble = true;
        }
    }

    function addStop(event) {
        if (!event.stop) event.stop = stopEvent;
        return event;
    }

    function connect(node, type, handler) {
        function wrapHandler(event) {
            handler(addStop(event || window.event));
        }

        if (typeof node.addEventListener == "function")
            node.addEventListener(type, wrapHandler, false);
        else
            node.attachEvent("on" + type, wrapHandler);
    }

    function forEach(arr, f) {
        for (var i = 0, e = arr.length; i < e; ++i) f(arr[i]);
    }

    function forEachInArray(arr, f) {
        var i = 0;
        while (arr[i] != undefined) {
            f(arr[i]);
            i++;
        }
    }

    getErrors();

});
