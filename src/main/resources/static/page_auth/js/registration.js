//    add a rule with a regular expression
jQuery.validator.addMethod(
    "regex",
    function (value, element, regexp) {
        if (regexp.constructor != RegExp)
            regexp = new RegExp(regexp);
        else if (regexp.global)
            regexp.lastIndex = 0;
        return this.optional(element) || regexp.test(value);
    }, "erreur expression reguliere"
);
$(document).ready(function () {

    //Khi bàn phím được nhấn và thả ra thì sẽ chạy phương thức này
    $("#formDemo").validate({
        rules: {
            firstName: "required",
            lastName: "required",

            password: {
                required: true,
                minlength: 8,
                maxlength: 32,
            },
            confirmPassword: {
                required: true,
                minlength: 8,
                equalTo: "#password",
            },
            email: {
                required: true,
                email: true,
            },
            username: {
                required: true,
                regex: /(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{1,}$/,
                minlength: 8,
                maxlength: 32,
            },
        },
        messages: {
            firstName: "This field is required",
            lastName: "This field is required",
            username: {
                required: "This field is required",
                regex: "At least 1 uppercase, 1 lowercase and 1 number",
            },
            email: {
                required: "This field is required",
                email: "Please enter valid email",
                pattern: "Please enter valid email",
            },
            password: {
                required: 'This field is required',
                minlength: 'At least 8 characters',
                maxlength: 'Maximum 32 characters',
            },
            confirmPassword: {
                required: "This field is required",
                minlength: 'At least 8 characters',
                equalTo: "Password does not match",
            },

        }
    });
});

$('.username').on('keyup', function () {
    //Check UserName existed when user type input
    if (this.value.length > 0) {
        var userName = this.value;
        //Check in DB system
        $.ajax({
            url: "/checkUserName",
            type: "POST",
            data: {
                userName: userName
            },
            success: function (value) {
                // If result = true, UserName exsts
                if (value == "true") {
                    // Message when UserName exists
                    $("#kqCheckName").text("User name exists in system !");
                    // Disable input in form sign up
                    var inputs = document.getElementsByClassName("disinput");
                    for (i = 0; i < inputs.length; i++) {
                        inputs[i].disabled = true;
                    }
                    // If result = false
                } else {
                    $("#kqCheckName").text("")
                    var inputs = document.getElementsByClassName("disinput");
                    for (i = 0; i < inputs.length; i++) {
                        inputs[i].disabled = false;
                    }
                }
            }
        })
    } else {
        $("#kqCheckName").text("");
    }
});
