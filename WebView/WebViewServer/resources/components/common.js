/**
 * Created by IntelliJ IDEA.
 * User: Natalia.Ukhorskaya
 * Date: 11/30/11
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
/*Messages*/
//Problems with ajax-request to server
var REQUEST_ABORTED = "Ajax request aborted.";
var HIGHLIGHT_REQUEST_ABORTED = "Can't get errors/warnings from server.";
var RUN_REQUEST_ABORTED = "Can't get program output from server.";
var COMPLETE_REQUEST_ABORTED = "Can't get completion proposal list from server.";
var EXAMPLES_REQUEST_ABORTED = "Can't get the example code from server.";
//Message in popup with warning before close tab with editor
var BEFORE_EXIT = "The changes you made to the program will be lost when this page is closed. Do you want to close the page?";
var ERROR_UNTIL_EXECUTE = "Your program has terminated with an exception.";
var TRY_RUN_CODE_WITH_ERROR = "Can't run a program with errors. See the Problems View tab.";
var EXECUTE_OK = "Compilation competed successfully.";
var GET_FROM_APPLET_FAILED = "Your system can't run Java Applets.";
var LOADING_EXAMPLE_OK = "Example is loaded.";
var COMPILE_IN_JS_APPLET_ERROR = "The Pre-Alpha JavaScript back-end could not generate code for this program.<br/>Try to run it using JVM.";
var SHOW_JAVASCRIPT_CODE = "Show generated JavaScript code";
var IE_SUPPORT = "Sorry, but now your browser doesn't supported. We recommended to use Mozilla Firefox or Google Chrome.";

var sessionId = -1;
var isApplet = false;
var isJsApplet = true;
var kotlinVersion;

var isAppletLoaded = false;

var isContentEditorChanged = false;

$(document).keydown(function (e) {
    if (e.keyCode == 120 && e.ctrlKey && e.shiftKey) {
        $("#runJS").click();
    } else if (e.keyCode == 120 && e.ctrlKey) {
        $("#run").click();
    }
});


function onBodyLoad() {
    if (navigator.appVersion.indexOf("MSIE") != -1) {
        document.getElementsByTagName("body")[0].innerHTML = IE_SUPPORT;
    } else {
        $("#help3").toggle(false);
        setSessionId();
        resizeCentral();
        setKotlinVersion('0.1.216');
        loadAccordionContent();
        loadHelpContentForExamples();
        hideLoader();
    }
}

function setSessionId() {

    var id;
    $.ajax({
        url:document.location.href + "getSessionId",
        context:document.body,
        type:"GET",
        dataType:"html",
        timeout:5000,
        success:getSessionIdSuccess
    });

}

function resizeCentral() {
    var wheight = (window.innerHeight) ? window.innerHeight :
        ((document.all) ? document.body.offsetHeight : null);
    document.getElementById("scroll").style.height = (wheight - 262 - 72 - 20 - 10) + "px";
    document.getElementById("left").style.minHeight = (wheight - 72 - 20 - 10) + "px";
    document.getElementById("right").style.minHeight = (wheight - 72 - 20 - 10) + "px";
    document.getElementById("center").style.minHeight = (wheight - 72 - 20 - 10) + "px";
    editor.refresh();
}

function getSessionIdSuccess(data) {
    var id = eval(data);
    var cookie = document.cookie;
    if (cookie != id) {
        document.cookie = "userId=" + id;
    }
    /*if (cookie == "") {
     document.cookie = id;
     } else {
     id = cookie;
     }*/
    sessionId = id;
    var info = "browser: " + navigator.appName + " " + navigator.appVersion;
    info += " " + "system: " + navigator.platform;
    $.ajax({
        url:document.location.href + "?sessionId=" + id + "&userData=true",
        context:document.body,
        type:"POST",
        data:{text:info},
        timeout:5000,
        error:function () {
            setStatusBarMessage(REQUEST_ABORTED);
        }
    });
}

function hideLoader() {
    $('#loader').hide();

}

function showLoader() {
//    document.getElementById("loader").style.display = 'block';
    $('#loader').show();
}

function setKotlinVersion(version) {
    $(".applet-disable").click();
    $(".applet-nohighlighting").click();
    $("#dialog").dialog({
        modal:"true",
        width:400,
        autoOpen:false
    });

    kotlinVersion = version;
    document.getElementById("kotlinVersionTop").innerHTML = "(" + kotlinVersion + ")";
    document.getElementById("kotlinVersion").innerHTML = kotlinVersion;
}

function setStatusBarMessage(message) {
    document.getElementById("statusbar").innerHTML = message;
}

function setConsoleMessage(message) {
    document.getElementById("console").innerHTML = message;
}


/*var hashLoc = location.hash;
 window.addEventListener("unload", beforeUnload, true);


 function beforeUnload(e) {
 if (location.hash != hashLoc) {
 if (confirm(BEFORE_EXIT)) {
 history.back();
 }
 }
 }*/

/*document.unload = function () {
 if (confirm(BEFORE_EXIT)) {
 history.back();
 }
 };*/

function beforeBack() {
    if (isContentEditorChanged && confirm(BEFORE_EXIT)) {
        history.back();
    }
    isContentEditorChanged = false;
//    bajb_backdetect.OnBack = beforeBack();
}


//bajb_backdetect.OnBack = beforeBack;

function unEscapeString(str) {
    str = str.replace(new RegExp("&amp;", 'g'), "&");
    return str;

}

var generatedJSCode = "";

function showJsCode() {
    document.getElementById("console").removeChild(document.getElementById("console").childNodes[1]);
    var consoleStr = document.getElementById("console").innerHTML;
    consoleStr += "<p class='consoleViewInfo'>" + generatedJSCode + "</p>";
    setConsoleMessage(consoleStr);
}

$("#whatimg").click(function () {
    $("#dialog").dialog("open");
});
