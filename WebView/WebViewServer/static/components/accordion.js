/**
 * Created by IntelliJ IDEA.
 * User: Natalia.Ukhorskaya
 * Date: 2/2/12
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */

var Accordion = (function () {
    function Accordion() {
        var instance = {
            loadContent1:function () {
                alert("a");
                var acc = document.createElement("div");
                acc.id = "accordion";
                document.getElementById("examplesaccordion").appendChild(acc);
                $.ajax({
                    url:generateAjaxUrl("loadExample", "all"),
                    context:document.body,
                    success:onLoadingExamplesSuccess,
                    dataType:"json",
                    type:"GET",
                    //data:{text:i},
                    timeout:10000,
                    error:function () {
                        setStatusBarMessage(EXAMPLES_REQUEST_ABORTED);
                    }
                });
            },
            detach:function () {

            }
        };

        return instance;
    }

    var lastSelectedExample = 0;

    Accordion.loadAllContent = function () {
        try {
            var acc = document.createElement("div");
            acc.id = "accordion";
            $("#examplesaccordion").append(acc);
            $.ajax({
                url:generateAjaxUrl("loadExample", "all"),
                context:document.body,
                success:onLoadingExamplesSuccess,
                dataType:"json",
                type:"GET",
                //data:{text:i},
                timeout:10000,
                error:function () {
                    setStatusBarMessage(EXAMPLES_REQUEST_ABORTED);
                }
            });
        } catch (e) {

        }
    };

    Accordion.getSelectedExample = function() {
        return lastSelectedExample;
    };

    Accordion.loadExample = function (name) {
        if (getEditorState()) {
            confirmAction(function (name) {
                return function () {
                    loadExample(name);
                }
            }(name));
        } else {
            loadExample(name);
        }
    };

    Accordion.loadProgram = function (name, isPublicLink) {
        if (getEditorState()) {
            confirmAction(function (name, isPublicLink) {
                return function () {
                    loadProgram(name, isPublicLink);
                }
            }(name, isPublicLink));
        } else {
            loadProgram(name, isPublicLink);
        }
    };

    Accordion.generatePublicLinkForExample = function (name) {
        if ($("div[id='pld" + name + "']").length <= 0) {
            var url = [location.protocol, '//', location.host, "/"].join('');
            var href = url + "?folder=" + name;
            setStatusBarMessage("Public link was generated.");
            $("a[id='" + name + "']").parent("p").after("<div class=\"toolbox\" id=\"pld" + name + "\" style=\"display:block;\"><div align=\"center\"><div class=\"fixedpage\"><div class=\"publicLinkHref\" id=\"pl" + name + "\"></div><img class=\"closePopup\" id=\"cp" + name + "\" src=\"/icons/close.png\" title=\"Close popup\"></span></div></div></div>");
            $("div[id='pl" + name + "']").html(href);
            $("img[id='cp" + name + "']").click(function () {
                $("div[id='pld" + name + "']").remove("div");
            });
            setStatusBarMessage(href);
        }
    };

    Accordion.generatePublicLinkForProgram = function (name) {
        if ($("div[id='pld" + name + "']").length <= 0) {
            $.ajax({
                url:generateAjaxUrl("generatePublicLink", name),
                context:document.body,
                success:onGeneratePublicLinkSuccess,
                dataType:"json",
                type:"GET",
                //data:{text:i},
                timeout:10000,
                error:function () {
                    setStatusBarMessage(PUBLIC_LINK_REQUEST_ABORTED);
                }
            });
        }
    };

    Accordion.deleteProgram = function (name) {
        if (confirm(BEFORE_DELETE_PROGRAM)) {
            if (lastSelectedExample == name) {
                lastSelectedExample = "";
            }

            $.ajax({
                url:generateAjaxUrl("deleteProgram", name),
                context:document.body,
                success:onDeletingProgramSuccess,
                dataType:"json",
                type:"GET",
                //data:{text:i},
                timeout:10000,
                error:function () {
                    setStatusBarMessage(DELETE_PROGRAM_REQUEST_ABORTED);
                }
            });
        }
    };

    Accordion.saveAsProgram = function () {
        if (isLogin) {
            $("#saveDialog").dialog("close");
            var i = editor.getValue();
            var programName = $("#programName").val();
            $("#programName").val("");
            var arguments = $("#arguments").val();

            $.ajax({
                url:generateAjaxUrl("saveProgram", programName + "&runConf=" + runConfiguration.mode),
                success:onSaveProgramSuccess,
                dataType:"json",
                type:"POST",
                data:{text:i, consoleArgs:arguments},
                timeout:10000,
                error:function () {
                    setStatusBarMessage(SAVE_PROGRAM_REQUEST_ABORTED);
                }
            });
        } else {
            $("#showInfoAboutLogin").click();
        }
    };

    Accordion.checkIfLoading = function () {
        return loadingExample;
    };

    Accordion.saveProgram = function () {
        if (isLogin) {
            if (lastSelectedExample == 0) {
                $("#saveAsProgram").click();
                return;
            }
            var folder = getFolderNameByUrl(lastSelectedExample);
            if (folder != "My_Programs") {
                $("#saveAsProgram").click();
                return;
            }

            var i = editor.getValue();
            var arguments = $("#arguments").val();
            $.ajax({
                url:generateAjaxUrl("saveProgram", "id=" + lastSelectedExample + "&runConf=" + runConfiguration.mode),
                success:onSaveProgramSuccess,
                dataType:"json",
                type:"POST",
                data:{text:i, consoleArgs:arguments},
                timeout:10000,
                error:function () {
                    setStatusBarMessage(SAVE_PROGRAM_REQUEST_ABORTED);
                }
            });
        } else {
            $("#showInfoAboutLogin").click();
        }
    };

    function onSaveProgramSuccess(data) {
        if (typeof data != "undefined" && data != null) {
            var i = 0;
            while (typeof data[i] != "undefined") {
                if (data[i].type == "exception") {
                    setStatusBarError(data[i].text);
                    setTimeout(function () {
                        $("#My_Programs").click();
                    }, 500);
                } else if (data[i].type == "programId") {
                    setEditorState(false);
                    setStatusBarMessage("Your program was successfully saved.");
                } else {
                    setEditorState(false);
                    var pos = data[i].text.indexOf("&id=");
                    if (pos > 0) {
                        var name = replaceAll(data[i].text.substring(0, pos), "%20", " ");
                        var id = data[i].text.substring(pos + 4);

                        setStatusBarMessage("Saved as: " + name + ".");
                        $("div#myprogramscontent").append(createProgramListElement(createExampleUrl(id, "My Programs"), name));
                        $("a#My_Programs").click();
                        $("problems").html("");
                        setConsoleMessage("");
                        removeStyles();
                        $("a[id='" + lastSelectedExample + "']").attr("class", "");
                        lastSelectedExample = createExampleUrl(id, "My Programs");
                        $("a[id='" + lastSelectedExample + "']").attr("class", "selectedExample");
                    }
                }
                i++;
            }
            editor.focus();
        }
    }

    function createProgramListElement(id, name, runConf) {
        var content = document.createElement("p");
        var span = document.createElement("div");
        span.className = "bullet";
//        span.innerHTML = "&#8226;";
        if (typeof runConf == "undefined") {
            span.style.background = "url(/icons/text.png) no-repeat";
        } else {
            span.style.background = "url(/icons/" + runConf + ".png) no-repeat";
        }
        content.appendChild(span);
        var contA = document.createElement("a");
        contA.id = id;
        contA.style.cursor = "pointer";
        contA.onclick = function () {
            beforeLoadProgram(this.id);
        };
        contA.innerHTML = name;
        var delImg = document.createElement("img");
        delImg.src = "/icons/delete.png";
        delImg.title = "Delete";
        delImg.onclick = function () {
            deleteProgram(this.parentNode.childNodes[1].id);
        };
        var linkImg = document.createElement("img");
        linkImg.src = "/icons/link1.png";
        linkImg.title = "Public link for this program";
        linkImg.onclick = function () {
            generatePublicLinkForProgram(this.parentNode.childNodes[1].id);
        };
        content.appendChild(contA);
        content.appendChild(delImg);
        content.appendChild(linkImg);

        return content;
    }


    function onDeletingProgramSuccess(data) {
        if (data != null && typeof data != "undefined") {
            if (data[0] != null && typeof data[0] != "undefined") {
                if (data[0].type == "exception") {
                    setStatusBarError(data[0].text);
                } else {
                    setStatusBarMessage(data[0].text);
                    document.getElementById(createExampleUrl(data[0].args, "My Programs")).parentNode.innerHTML = "";

                }
            }
        }
    }

    function onGeneratePublicLinkSuccess(data) {
        if (data != null && typeof data != "undefined") {
            if (data[0] != null && typeof data[0] != "undefined") {
                if (data[0].type == "exception") {
                    setStatusBarError(data[0].text);
                } else {
                    setStatusBarMessage("Public link was generated.");

                    var programId = data[0].text.substring(data[0].text.indexOf("publicLink=") + 11);
                    var name = createExampleUrl(programId, "My Programs");
                    var displayedName = $("a[id='" + name + "']").html();
                    var tweetHref = escape("#Solved " + displayedName + " " + data[0].text + " #Kotlin");
                    $("a[id='" + name + "']").parent("p").after("<div class=\"toolbox\" id=\"pld" + name + "\" style=\"display:block;\"><div align=\"center\"><div class=\"fixedpage\"><div class=\"publicLinkHref\" id=\"pl" + name + "\"></div><a target=\"_blank\" href=\"http://twitter.com/home?status=" + tweetHref + "\"><img class=\"tweetImg\" src=\"/images/social/twitter.png\"/></a><img class=\"closePopup\" id=\"cp" + name + "\" src=\"/icons/close.png\" title=\"Close popup\"></span></div></div></div>");
                    $("div[id='pl" + name + "']").html(data[0].text);
                    $("img[id='cp" + name + "']").click(function () {
                        $("div[id='pld" + name + "']").remove("div");
                    });

                }
            }
        }
    }

    var loadingExample = false;

    function loadProgram(name, isPublicLink) {
        if (lastSelectedExample == name) {
            return;
        }

        clearProblemView();
        setConsoleMessage("");
        removeStyles();

        $("a[id='" + lastSelectedExample + "']").attr("class", "");
        lastSelectedExample = name;
        $("a[id='" + name + "']").attr("class", "selectedExample");

        $("#My_Programs").click();

        setStatusBarMessage("Loading program...");
        loadingExample = true;
        if (isPublicLink) {
            name += "publicLink";
        }
        $.ajax({
            url:generateAjaxUrl("loadProgram", name),
            context:document.body,
            success:onLoadingProgramSuccess,
            dataType:"json",
            type:"GET",
            //data:{text:i},
            timeout:10000,
            error:function () {
                setStatusBarMessage(LOAD_PROGRAM_REQUEST_ABORTED);
            }
        });

    }

    function onLoadingProgramSuccess(data) {
        editor.focus();
        loadingExample = false;

        if (data != null && typeof data != "undefined") {
            if (data[0] != null && typeof data[0] != "undefined") {
                if (data[0].type == "exception") {
                    setStatusBarError(data[0].text);
                } else {
                    editor.setValue(data[0].text);
                    $("#arguments").val(data[0].args);
                    if (data[0].runConf == "") {
                        $("#runConfigurationMode").selectmenu("value", "java");
                    } else {
                        $("#runConfigurationMode").selectmenu("value", data[0].runConf);
                    }
                    setStatusBarMessage(LOADING_PROGRAM_OK);

                    $("a[id='" + lastSelectedExample + "']").attr("class", "selectedExample");
                }
            }
        }
        setEditorState(false);
    }


    function loadExample(name) {
        if (lastSelectedExample == name) {
            return;
        }

        loadExampleHelp(getNameByUrl(name));

        clearProblemView();
        setConsoleMessage("");
        removeStyles();

        $("a[id='" + lastSelectedExample + "']").attr("class", "");
        lastSelectedExample = name;
        $("a[id='" + name + "']").attr("class", "selectedExample");

        setStatusBarMessage("Loading program...");
        loadingExample = true;

        $.ajax({
            url:generateAjaxUrl("loadExample", name),
            context:document.body,
            success:onLoadingExampleSuccess,
            dataType:"json",
            type:"GET",
            timeout:10000,
            error:function () {
                loadingExample = false;
                setStatusBarMessage(EXAMPLES_REQUEST_ABORTED);
            }
        });

    }

    var counter = 0;

    function onLoadingExampleSuccess(data) {
        if (runConfiguration.mode == "" && counter < 5) {
            setTimeout(function () {
                counter++;
                onLoadingExampleSuccess(data);
            }, 50);
        } else {
            counter = 0;
//        editor.focus();
            loadingExample = false;
            if (typeof data[0] != "undefined") {
                editor.setValue(data[0].text);
                setStatusBarMessage(LOADING_EXAMPLE_OK);
            }
            setEditorState(false);
        }
    }

    function onLoadingExamplesSuccess(data) {

        var acc = document.getElementById("accordion");
        var i = 0;
        while (typeof data[i] != "undefined") {
            var lastFolderName;
            var ids = [];
            if (data[i].type == "folder") {
                var folder = document.createElement("h3");
                var folderA = document.createElement("a");
                folderA.href = "#";
                folderA.id = replaceAll(data[i].text, " ", "_");

                folderA.innerHTML = data[i].text;
                lastFolderName = data[i].text;
                folder.appendChild(folderA);
                acc.appendChild(folder);
                var cont = document.createElement("div");
            }
            if (data[i].type == "content") {
                var content = document.createElement("p");
                var span = document.createElement("div");
                span.className = "bullet";
                span.style.background = "url(/icons/java.png) no-repeat";
                //background: url(/icons/java.png) repeat-x;
                span.id = "bullet" + replaceAll(data[i].text, " ", "_");
//                span.innerHTML = "&#8226;";
                content.appendChild(span);
                var contA = document.createElement("a");
                contA.id = createExampleUrl(data[i].text, lastFolderName);
                contA.style.cursor = "pointer";
                contA.onclick = function () {
                    beforeLoadExample(this.id);
                };
                contA.innerHTML = data[i].text;
                var linkImg = document.createElement("img");
                linkImg.src = "/icons/link1.png";
                linkImg.title = "Public link for this example";
                linkImg.onclick = function () {
                    generatePublicLinkForExample(this.parentNode.childNodes[1].id);
                };

                content.appendChild(contA);
                content.appendChild(linkImg);
                cont.appendChild(content);
            }
            acc.appendChild(cont);

            i++;
        }

        var myProg = document.createElement("h3");
        var innerDiv = document.createElement("div");
        innerDiv.id = "tools";
        var myProgA = document.createElement("a");
        myProgA.href = "#";
        myProgA.id = "My_Programs";

        myProgA.innerHTML = "My Programs";
        innerDiv.appendChild(myProgA);

        if (isLogin) {
            var saveImg = document.createElement("img");
            saveImg.src = "/icons/save1.png";
            saveImg.id = "saveProgram";
            if (!isMac) {
                saveImg.title = "Save current program (Ctrl + S)";
            } else {
                saveImg.title = "Save current program (Cmd + S)";
            }
            var saveAsImg = document.createElement("img");
            saveAsImg.src = "/icons/saveAs1.png";
            saveAsImg.id = "saveAsProgram";
            saveAsImg.title = "Save current program as ...";
            innerDiv.appendChild(saveAsImg);
            innerDiv.appendChild(saveImg);
        } else {
            var infoImg = document.createElement("img");
            infoImg.src = "/images/whatisthis.png";
            infoImg.id = "showInfoAboutLogin";
            infoImg.title = "Help";
            innerDiv.appendChild(infoImg);
        }
        myProg.appendChild(innerDiv);
        acc.appendChild(myProg);

        var myProgCont = document.createElement("div");
        myProgCont.id = "myprogramscontent";
        acc.appendChild(myProgCont);

        if (isLogin) {
            loadListOfPrograms();
        }

        setRunConfForAllExamples();

        $("#accordion").accordion({
            autoHeight:false,
            navigation:true
        }).find('#tools img').click(function (ev) {
                ev.preventDefault();
                if (this.id == "saveProgram") {
                    save();
                } else if (this.id == "saveAsProgram") {
                    $("#saveDialog").dialog("open");
                } else if (this.id == "showInfoAboutLogin") {
                    $("#showInfoAboutLoginDialog").dialog("open");
                }
            });


        var urlAct = [location.protocol, '//', location.host, "/"].join('');
        var url = document.location.href;
        if (url.indexOf(urlAct) != -1) {
            url = url.substring(url.indexOf(urlAct) + urlAct.length);
            var exampleStr = "?folder=";
            var publicLink = "?publicLink=";
            if (url.indexOf(exampleStr) == 0) {
//            url = url;
                url = url.substring(exampleStr.length);
                $("#" + getFolderNameByUrl(url)).click();
                beforeLoadExample(url);
            } else if (url.indexOf(publicLink) == 0) {
//            url = url;
                //$("#" + url.substring(url.indexOf("&folder=") + 6)).click();
                beforeLoadProgram(createExampleUrl(url.substring(publicLink.length), "My Programs"));
                //url.substring(publicLink.length) + "&folder=My_Programs", true);
            } else {
                beforeLoadExample("Hello,_world!&name=Simplest_version");
            }
        }
    }

    function loadListOfPrograms() {

        $.ajax({
            url:generateAjaxUrl("loadProgram", "all"),
            context:document.body,
            success:onLoadingProgramsSuccess,
            dataType:"json",
            type:"GET",
            //data:{text:i},
            timeout:10000,
            error:function () {
                setStatusBarMessage(LOAD_PROGRAM_REQUEST_ABORTED);
            }
        });
    }

    function onLoadingProgramsSuccess(data) {
        var i = 0;
        var cont = document.getElementById("myprogramscontent");
        while (typeof data[i] != "undefined") {
            $("#myprogramscontent").append(createProgramListElement(createExampleUrl(data[i].id, "My Programs"), replaceAll(data[i].name, "%20", " ")));
            i++;
        }
    }

    return Accordion;
})();