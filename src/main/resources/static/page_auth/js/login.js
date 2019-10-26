new WOW().init();

function printError(elemId, hintMsg) {
    document.getElementById(elemId).innerHTML = hintMsg;
}

function validate() {

    if (document.f.username.value == "" && document.f.password.value == "") {
        printError("bothErr", "Username and Password are required");
        document.f.username.focus();
        return false;
    } else {
        printError("bothErr", "");
        document.f.username.focus();
        bothErr = false;
    }
    if (document.f.username.value == "") {
        printError("nameErr", "Please enter your username");
        document.f.username.focus();
        return false;
    } else {
        printError("nameErr", "");
        document.f.username.focus();
        nameErr = false;
    }
    if (document.f.password.value == "") {
        printError("pwErr", "Please enter your password");
        document.f.password.focus();
        return false;
    } else {
        printError("pwErr", "");
        document.f.password.focus();
        pwErr = false;
    }

}

